package myTest;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class listSortTest {
    /**
     * 按照List中的某个Int类型的属性进行排序
     * @param list
     */
    @SuppressWarnings("unchecked")
    public static void sortIntMethod(List list){
        Collections.sort(list, new Comparator(){
            @Override
            public int compare(Object o1, Object o2) {
                Student stu1=(Student)o1;
                Student stu2=(Student)o2;
                if(stu1.getAge()>stu2.getAge()){
                    return 1;
                }else if(stu1.getAge()==stu2.getAge()){
                    return 0;
                }else{
                    return -1;
                }
            }
        });
        System.out.println("/////////////排序之后///////////////");
        System.out.println(list);
        for(int i=0;i<list.size();i++){
            Student st=(Student)list.get(i);
            System.out.println("st.age="+st.getAge()+",st.name="+st.getName());
        }
    }
}
