import com.firefly.c45.C45;
import com.firefly.c45.C45Util;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        String[] title=new String[]{
            "天气","温度","湿度","风速","活动"
        };
        List<C45Util.ValItem> vals=new ArrayList<C45Util.ValItem>();
        vals.add(new C45Util.ValItem(new String[]{"晴","炎热","高","弱"},"取消"));
        vals.add(new C45Util.ValItem(new String[]{"晴","炎热","高","强"},"取消"));
        vals.add(new C45Util.ValItem(new String[]{"阴","炎热","高","弱"},"进行"));
        vals.add(new C45Util.ValItem(new String[]{"雨","适中","高","弱"},"进行"));
        vals.add(new C45Util.ValItem(new String[]{"雨","寒冷","正常","弱"},"进行"));
        vals.add(new C45Util.ValItem(new String[]{"雨","寒冷","正常","强"},"取消"));
        vals.add(new C45Util.ValItem(new String[]{"阴","寒冷","正常","强"},"进行"));
        vals.add(new C45Util.ValItem(new String[]{"晴","适中","高","弱"},"取消"));
        vals.add(new C45Util.ValItem(new String[]{"晴","寒冷","正常","弱"},"进行"));
        vals.add(new C45Util.ValItem(new String[]{"雨","适中","正常","弱"},"进行"));
        vals.add(new C45Util.ValItem(new String[]{"晴","适中","正常","强"},"进行"));
        vals.add(new C45Util.ValItem(new String[]{"阴","适中","高","强"},"进行"));
        vals.add(new C45Util.ValItem(new String[]{"阴","炎热","正常","弱"},"进行"));
        vals.add(new C45Util.ValItem(new String[]{"雨","适中","高","强"},"取消"));

//        C45Util.InfoVal[] gain= C45Util.igr(vals,new boolean[]{false,true,true,true});
//        for(int i=0;i<gain.length;i++){
//            if(gain[i]!=null){
//                System.out.println("Gain("+title[i]+")="+gain[i].getVal());
//            }
//        }

        C45 c45=new C45(vals);
        c45.train();


        vals=new ArrayList<C45Util.ValItem>();
        vals.add(new C45Util.ValItem(new String[]{"晴","适中","高","弱"},"取消"));
        vals.add(new C45Util.ValItem(new String[]{"晴","适中","正常","强"},"取消"));
        vals.add(new C45Util.ValItem(new String[]{"阴","炎热","高","强"},"进行"));
        vals.add(new C45Util.ValItem(new String[]{"阴","适中","正常","弱"},"进行"));
        out(c45,vals);
    }

    private static void out(C45 c45,List<C45Util.ValItem> vals){
        double lost=0;
        for(C45Util.ValItem item:vals){
            String label=c45.fit(item.getX());
            if(!label.equals(item.getY())){
                lost++;
            }
            System.out.println("预测："+label+",实际："+item.getY());
        }
        System.out.println("正确率为："+(vals.size()-lost)/vals.size());
    }
}
