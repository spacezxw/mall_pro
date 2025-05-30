package com.macro.mall.portal.domain;

import com.macro.mall.model.SmsCoupon;
import com.macro.mall.model.SmsCouponHistory;
import com.macro.mall.model.SmsCouponProductCategoryRelation;
import com.macro.mall.model.SmsCouponProductRelation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 优惠券领取历史详情（包括优惠券信息和关联关系）
 * Created by macro on 2018/8/29.
 */
@Getter
@Setter
public class SmsCouponHistoryDetail extends SmsCouponHistory {
    @Schema(title = "相关优惠券信息")
    private SmsCoupon coupon;
    @Schema(title = "优惠券关联商品")
    private List<SmsCouponProductRelation> productRelationList;
    @Schema(title = "优惠券关联商品分类")
    private List<SmsCouponProductCategoryRelation> categoryRelationList;
}
