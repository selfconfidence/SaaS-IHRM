package $ import javax.persistence.Entity;
import javax.persistence.Table;

{pPackage}.pojo;

@Entity
@Table(name = "${table.name}")
public class ${ClassName} implements Serializable {

    <#list table.columns as column>
    <#if column.columnKey??>
    @Id
    </#if>
    private ${column.columnType} ${column.columnName2};
    </#list>

    <#list table.columns as column>
    public void set${column.columnName2?cap_first}(${column.columnType} value) {
        this.${column.columnName2} = value;
    }
    public ${column.columnType} get${column.columnName2?cap_first}() {
       return this.${column.columnName2};
    }
    </#list>
}
