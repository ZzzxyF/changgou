package com.changgou.search.pojo;


import com.zxy.pojo.Sku;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;



import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Document(indexName = "skuinfo",type = "docs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SkuInfo  implements Serializable {
    @Id
    private Long id;//商品id

    @Field(type=FieldType.Text,analyzer = "ik_max_word")
    private String name;//SKU名称

    @Field(type=FieldType.Double)
    private Integer price;//价格（分）

    private Integer num;//库存数量

    private String image;//商品图片

    private String status;//商品状态 1-正常，2-下架，3-删除

    private Date createTime;//创建时间

    private Date updateTime;//更新时间

    private String isDefault;//是否默认

    private Long spuId;//SPUID

    private Integer categoryId;//类目ID

    @Field(type=FieldType.Keyword)
    private String categoryName;//类目名称

    @Field(type=FieldType.Keyword)
    private String brandName;//品牌名称

    private String spec;//规格

    private Map<String,Object> specMap;//规格参数

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Map<String, Object> getSpecMap() {
        return specMap;
    }

    public void setSpecMap(Map<String, Object> specMap) {
        this.specMap = specMap;
    }
}
