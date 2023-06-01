package com.zxy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

//商品pojo --》一个商品具有一个spu、和多个规格sku
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Goods implements Serializable {

    private Spu spu;

    private List<Sku> sku;

}
