package com.fairplay.examgradle.bean;

import java.util.List;

public class ItemInfoBean extends BaseBean<List<ItemInfoBean.ItemInfo>> {

    public static class ItemInfo{
        public String itemName;       // 项目名称
        public String examItemCode;   // 项目代码
        public String resultUnit;     // 成绩单位
        public int itemType;           // 体侧项目类型(0: 身高、体重, 1: 肺活量, 2: 耐⼒, 3: 柔韧, 4 : 速度, 5: 视⼒)
        public int testType;          // 测量方式(计时 1: 计数, 2: 远度, 3: ⼒量, 4: 台阶 5)
        public int resultTestNum;  // 考试次数
        public int itemRoundNum;   // 单次考试轮次数量
        public int decimalDigits;  // ⼩数位数
        public int carryMode;    // 进位方式 (1: 四舍五⼊, 2: 舍位, 3: 非零进取)
        public int scoreCountMode;  // 算分模式（0:精确算分1:固定算分）
        public int scoreCountRule;  // 得分固定算分规则（0:往低分靠1:往高分靠）
        public int scoreCarryMode;   // 得分进位方式 （1.四舍五⼊ 2.舍位 3.非零进取）
        public int scoreDecimalDigits;   // 得分⼩数位数
        public int scoreCarryByHundredth;  // 得分根据后⼏位进位
        public String minResult;  // 最⼩成绩
        public String maxResult;  // 最⼤成绩
        public int lastResultMode; // 最终成绩选择模式 (1: 最后成绩, 2: 补考成绩, 3: 最好)
        public String machineCode; // 机器码
        public int itemProperty;  // 项目属性(1: 必考, 2: 选考)
        public int exmItemType;  // 考试项目类型(0:素质，1:专项)
        public int limitGender;  // 限报性别(0:男, 1:⼥, 2:男⼥混合)
        public String minScore;    // 最低分
        public int ratio;   // 分值比例系数
        public int isLowestPoint;  // 是否给最低分,0:否，1:是
        public int faceRecognitionMode;   // 检录模式(0: 按⼈检录, 1: 按组检录)
        public String[] placeName;    // 场地名称,数组
        public List<ExtBody> extBody;
        public List<ItemTag> itemTag;     // 项目自定义标签
        public List<SubItem> subitemList;  // ⼦项列表

        @Override
        public String toString() {
            return "ItemInfo{" +
                    "itemName='" + itemName + '\'' +
                    ", examItemCode='" + examItemCode + '\'' +
                    ", resultUnit='" + resultUnit + '\'' +
                    ", itemType=" + itemType +
                    ", testType=" + testType +
                    ", resultTestNum=" + resultTestNum +
                    ", itemRoundNum=" + itemRoundNum +
                    ", decimalDigits=" + decimalDigits +
                    ", carryMode=" + carryMode +
                    ", scoreCountMode=" + scoreCountMode +
                    ", scoreCountRule=" + scoreCountRule +
                    ", scoreCarryMode=" + scoreCarryMode +
                    ", scoreDecimalDigits=" + scoreDecimalDigits +
                    ", scoreCarryByHundredth=" + scoreCarryByHundredth +
                    ", minResult=" + minResult +
                    ", maxResult=" + maxResult +
                    ", lastResultMode=" + lastResultMode +
                    ", machineCode='" + machineCode + '\'' +
                    ", itemProperty=" + itemProperty +
                    ", exmItemType=" + exmItemType +
                    ", limitGender=" + limitGender +
                    ", minScore=" + minScore +
                    ", ratio=" + ratio +
                    ", isLowestPoint=" + isLowestPoint +
                    ", faceRecognitionMode=" + faceRecognitionMode +
                    ", placeName=" + placeName +
                    ", extBody=" + extBody +
                    ", itemTag=" + itemTag +
                    ", subitemList=" + subitemList +
                    '}';
        }
    }
    public static class ExtBody{
        public String extName;
        public String belongType;   // 归属类型(1: 考试, 2: 体侧)
        public String extType;   // 扩展值类型(0:单个值 1:多个值)
        public List<String> extValue;  // 值列表

        @Override
        public String toString() {
            return "ExtBody{" +
                    "extName='" + extName + '\'' +
                    ", belongType='" + belongType + '\'' +
                    ", extType='" + extType + '\'' +
                    ", extValue=" + extValue +
                    '}';
        }
    }

    public static class ItemTag{
        public String tagName;    // 标签名称
        public String tagAttrMajor; // 标签主要属性
        public List<TagAttr> tagAttrList;  // 标签属性列表

        @Override
        public String toString() {
            return "ItemTag{" +
                    "tagName='" + tagName + '\'' +
                    ", tagAttrMajor='" + tagAttrMajor + '\'' +
                    ", tagAttrList=" + tagAttrList +
                    '}';
        }
    }
    public static class TagAttr{
        public String name;
        public String desc;
        public String groupCount;

        @Override
        public String toString() {
            return "TagAttr{" +
                    "name='" + name + '\'' +
                    ", desc='" + desc + '\'' +
                    ", groupCount='" + groupCount + '\'' +
                    '}';
        }
    }
    public static class SubItem{
        public String subitemName;   // ⼦项目名称
        public String subitemCode;  // ⼦项目代码
        public List<SubitemTag> subitemTag;  // ⼦项目标签属性
        public List<ExtBody> extBody;  // 项目扩展属性
        public MultipleValueSetting multipleValueSetting;
        public int subitemType;     // 分项类型(1: 专项素质, 1: 专项技术, 2: 实战能⼒, 3: 跳跃技术, 4: 自然素质, 5: 成套动作)
        public int subitemTestType;    // 测量方式(计时:1，计数:2，远度:3，⼒量:4, 台阶:5)
        public String subitemUnit;   // 成绩单位
        public int subitemTestNum;  // 考试次数
        public int subitemRoundNum;  // 单次考试轮次数量
        public int scoreMultiple;  // 分值倍数
        public int decimalDigits;  // ⼩数位数
        public int carryMode;   // 进位方式 (1: 四舍五⼊ 2: 舍位 3: 非零进取)
        public int scoreCountMode;   // 算分模式（0:精确算分1:固定算分）
        public int scoreCountRule;  // 得分固定算分规则（0:往低分靠1:往高分靠）
        public int scoreCarryMode;  // 得分进位方式 （1.四舍五⼊ 2.舍位 3.非零进取）
        public int scoreDecimalDigits;  // 得分⼩数位数
        public int scoreCarryByHundredth;  // 得分根据后⼏位进位
        public int markScore;     // 打分类型 (0: 测量项目，1: 打分项目)
        public int scoreCount;   // 打分个数，超过1的时候算分方式才生效
        public int calScoreType;  // 算分方式 (0: 取最好, 1.去掉最高最低取平均分, 2: 取平均分)
        public int calScoreExpression;  // 算分表达式
        public int enableTempGroup;  // 启用临时分组(0: 不启用, 1: 启用)
        public int enableTempGroupRight;  // 启用临时分组后设置测量用户给临时分组的打分权限(0: 不启用, 1: 启用)
        public int faceRecognitionMode;  // 检录模式(0: 按⼈检录, 1: 按组检录)
        public String minResult;   // 最⼩成绩
        public String maxResult;   // 最⼤成绩
        public String machineCode;  // 机器码
        public String minScore;  // 最低分
        public double ratio;   // 分值比例系数
        public int isLowestPoint;  // 是否给最低分(0: 否, 1: 是)

        @Override
        public String toString() {
            return "SubItem{" +
                    "subitemName='" + subitemName + '\'' +
                    ", subitemCode='" + subitemCode + '\'' +
                    ", subitemTag=" + subitemTag +
                    ", extBody=" + extBody +
                    ", multipleValueSetting=" + multipleValueSetting +
                    ", subitemType=" + subitemType +
                    ", subitemTestType=" + subitemTestType +
                    ", subitemUnit='" + subitemUnit + '\'' +
                    ", subitemTestNum=" + subitemTestNum +
                    ", subitemRoundNum=" + subitemRoundNum +
                    ", scoreMultiple=" + scoreMultiple +
                    ", decimalDigits=" + decimalDigits +
                    ", carryMode=" + carryMode +
                    ", scoreCountMode=" + scoreCountMode +
                    ", scoreCountRule=" + scoreCountRule +
                    ", scoreCarryMode=" + scoreCarryMode +
                    ", scoreDecimalDigits=" + scoreDecimalDigits +
                    ", scoreCarryByHundredth=" + scoreCarryByHundredth +
                    ", markScore=" + markScore +
                    ", scoreCount=" + scoreCount +
                    ", calScoreType=" + calScoreType +
                    ", calScoreExpression=" + calScoreExpression +
                    ", enableTempGroup=" + enableTempGroup +
                    ", enableTempGroupRight=" + enableTempGroupRight +
                    ", faceRecognitionMode=" + faceRecognitionMode +
                    ", minResult=" + minResult +
                    ", maxResult=" + maxResult +
                    ", machineCode='" + machineCode + '\'' +
                    ", minScore=" + minScore +
                    ", ratio=" + ratio +
                    ", isLowestPoint=" + isLowestPoint +
                    '}';
        }
    }
    public static class SubitemTag{
        public String tagName;
        public String tagAttrMajor;
        public TagAttr tagAttrValue;

        @Override
        public String toString() {
            return "SubitemTag{" +
                    "tagName='" + tagName + '\'' +
                    ", tagAttrMajor='" + tagAttrMajor + '\'' +
                    ", tagAttrValue=" + tagAttrValue +
                    '}';
        }
    }
    public static class MultipleValueSetting{
        public String calScoreType; // 算分方式-0.取最好，1.去掉最高，最低，中间取平均分 2: 取平均分, 3: 求和
        public List<Value> valueList;

        @Override
        public String toString() {
            return "MultipleValueSetting{" +
                    "calScoreType='" + calScoreType + '\'' +
                    ", valueList=" + valueList +
                    '}';
        }
    }
    public static class Value{
        public String order;  // 值顺序
        public String group;   // 值分组，多个值可能归属⼀个组显示
        public String desc;  // 值描述  左  右
        public String score;   // 得分
        public String machineScore;  // 机器得分

        @Override
        public String toString() {
            return "Value{" +
                    "order='" + order + '\'' +
                    ", group='" + group + '\'' +
                    ", desc='" + desc + '\'' +
                    ", score='" + score + '\'' +
                    ", machineScore='" + machineScore + '\'' +
                    '}';
        }
    }
}
