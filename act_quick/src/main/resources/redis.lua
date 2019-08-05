local key = KEYS[1] --限流KEY（一秒一个）
local limit = tonumber(ARGV[1]) --限流大小
local current = tonumber(redis.call('get', key) or "0")
if current + 1 > limit then --如果超出限流大小
    return 0
else --请求数+1，并设置1秒过期,过期之后不在重新走向限流的流程
    redis.call("INCRBY", key,"1")
    redis.call("expire", key,"1")
end
return 1