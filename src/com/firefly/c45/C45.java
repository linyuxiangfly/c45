package com.firefly.c45;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class C45 {
    private static double log2=Math.log(2);
    private List<C45Util.ValItem> valItems;
    private Node rootNode;

    public C45(List<C45Util.ValItem> valItems){
        this.valItems=valItems;
    }

    /**
     * 训练
     */
    public void train(){
        boolean[] validColumns=new boolean[valItems.get(0).x.length];
        for(int i=0;i<validColumns.length;i++){
            validColumns[i]=true;
        }

        //创建节点
        rootNode=createNode(valItems,-1,validColumns);
    }

    private Node createNode(List<C45Util.ValItem> valItems,int column,boolean[] validColumns){
        Node node=new Node();
        //是否纯净的
        boolean isPure=pure(valItems);

        node.setLeaf(isPure);

        if(isPure){
            node.setColumn(column);
            //返回标签值
            node.setVal(valItems.get(0).getY());
        }else{
            C45Util.InfoVal[] gain= C45Util.igr(valItems,validColumns);
            int index=maxInfoValIndex(gain);
            validColumns[index]=false;
            node.setColumn(index);

            Map<String,Node> map=new HashMap<String, Node>();
            //循环字段所有可能的值
            for(Map.Entry<String,List<C45Util.ValItem>> entry:gain[index].getCls().entrySet()){
                //递归创建节点
                map.put(entry.getKey(),createNode(entry.getValue(),index,validColumns));
            }

            node.setVal(map);
        }

        return node;
    }

    /**
     * 是否纯净的
     * @return
     */
    private boolean pure(List<C45Util.ValItem> valItems){
        String label=valItems.get(0).getY();

        for(C45Util.ValItem item:valItems){
            if(!item.getY().equals(label)){
                return false;
            }
        }
        return true;
    }

    /**
     * 获取最大的下标
     * @param infoVals
     * @return
     */
    private int maxInfoValIndex(C45Util.InfoVal[] infoVals){
        int index=-1;
        double max=0;
        if(infoVals!=null){
            for(int i=0;i<infoVals.length;i++){
                if(infoVals[i]!=null && infoVals[i].getVal()>max){
                    max=infoVals[i].getVal();
                    index=i;
                }
            }
        }

        return index;
    }

    /**
     * 匹配数据
     * @return
     */
    public String fit(String[] val){
        return fit(rootNode,val).getVal().toString();
    }

    private Node fit(Node node,String[] vals){
        if(node!=null && node.isLeaf()){
            return node;
        }else{
            Map<String,Node> map=(Map)node.getVal();
            return fit(map.get(vals[node.getColumn()]),vals);
        }
    }

    /**
     * 节点
     */
    public class Node{
        private boolean leaf=false;//是否为叶子
        private int column=0;//字段号
        private Object val;//值

        public Node(){

        }

        public Node(boolean leaf, int column, Object val) {
            this.leaf = leaf;
            this.column = column;
            this.val = val;
        }

        public boolean isLeaf() {
            return leaf;
        }

        public void setLeaf(boolean leaf) {
            this.leaf = leaf;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        public Object getVal() {
            return val;
        }

        public void setVal(Object val) {
            this.val = val;
        }
    }
}
