
### act_spring 模块是activiti与springBoot的整合,里面包含了详细的整合操作.足够应用与开发


> 在springBoot应用整合actitivi 的时候 官方提出了2个类去整合所有的Service 例如 ProcessRunTime TaskRunTime

> 在引用这两个操作类的同时需要记住,这个是和SpringSecurity是强依赖作用.

> 在创建bpmn的时候 根据官方文档指定 存储在 /resources/processes 下的 *.bpmn 会自动部署

> 同时解释Activiti 中的用户组的应用

>使用SpringBoot 的时候默认创建的流程表是没有关于 hi 表记录的,未知原因,是不生效的. email call actitivi develepPerson