package com.firefly.c45;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * C4.5
 */
public class C45Util {
    public static InfoVal[] igr(List<ValItem> vals){
        return igr(vals,null);
    }

    /**
     * 信息增益率
     * @param vals 数据
     * @return
     */
    public static InfoVal[] igr(List<ValItem> vals,boolean[] validColumns){
        InfoVal infoY=info(vals,-1,true);//计算类别信息熵
        InfoVal[] infoX=new InfoVal[vals.get(0).x.length];
        for(int i=0;i<infoX.length;i++){
            //是否有有效列信息
            if(validColumns==null || (validColumns.length>i && validColumns[i])){
                InfoVal ivF=info(vals,i,false);
                InfoVal ivT=info(vals,i,true,ivF.getCls());

                ivF.setVal((infoY.getVal()-ivF.getVal())/ivT.getVal());

                infoX[i]=ivF;
            }
        }
        return infoX;
    }

    private static InfoVal info(List<ValItem> vals, int column, boolean meas){
        return info(vals,column,meas,null);
    }

    private static InfoVal info(List<ValItem> vals, int column, boolean meas,Map<String, List<ValItem>> cls){
        double ret=0;
        double log2=Math.log(2);
        double count=vals.size();

        if(cls==null){
            cls=classification(vals,column);
        }

        //信息度量
        if(meas){
            for(Map.Entry<String,List<ValItem>> entry:cls.entrySet()){
                double v=entry.getValue().size()/count;
                ret+=-v*Math.log(v)/log2;
            }
        }
        //属性的信息熵
        else{
            for(Map.Entry<String,List<ValItem>> entry:cls.entrySet()){
                InfoVal infoVal=info(entry.getValue(),-1,true);
                ret+=
                        entry.getValue().size()/count*
                                infoVal.getVal();
                //清除MAP
                clearMap(infoVal.getCls());
            }
        }

        return new InfoVal(ret,cls);
    }

    private static void clearMap(Map<String, List<ValItem>> map){
        for(Map.Entry<String,List<ValItem>> entry:map.entrySet()){
            entry.getValue().clear();
        }
        map.clear();
    }

    /**
     * 归类
     * @param vals
     * @param column,-1为y值，大于等于0为x的列数
     * @return
     */
    private static Map<String, List<ValItem>> classification(List<ValItem> vals, int column){
        Map<String, List<ValItem>> ret=new HashMap<String, List<ValItem>>();
        for(ValItem valItem:vals){
            String key;

            //根据字段分类
            if(column<=-1){
                key=valItem.y;
            }else{
                key=valItem.x[column];
            }

            List<ValItem> valItems=ret.get(key);

            if(valItems==null){
                valItems=new ArrayList<ValItem>();
                ret.put(key,valItems);
            }
            valItems.add(valItem);
        }
        return ret;
    }

    public static class ValItem{
        String[] x;
        String y;

        public ValItem(String[] x, String y) {
            this.x = x;
            this.y = y;
        }

        public String[] getX() {
            return x;
        }

        public void setX(String[] x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }
    }

    /**
     * 信息值
     */
    public static class InfoVal{
        private double val;
        private Map<String, List<ValItem>> cls=null;

        public InfoVal() {

        }

        public InfoVal(double val, Map<String, List<ValItem>> cls) {
            this.val = val;
            this.cls = cls;
        }

        public double getVal() {
            return val;
        }

        public void setVal(double val) {
            this.val = val;
        }

        public Map<String, List<ValItem>> getCls() {
            return cls;
        }

        public void setCls(Map<String, List<ValItem>> cls) {
            this.cls = cls;
        }
    }
}
