package OnlineJudge.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
//    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        //完成方法分发
//        //1.获取请求路径
//        String uri= request.getRequestURI();
//        //2.获取方法名称
//        String methodName=uri.substring(uri.lastIndexOf('/')+1);
//
//        //3.获取方法对象Method
//        try {
//            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
//            //4.执行方法
//            method.invoke(this, request, response);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String uri= req.getRequestURI();
        String name = uri.substring(uri.lastIndexOf('/')+1);uri.substring(uri.lastIndexOf('/')+1);//获取方法名
        if(name == null || name.isEmpty()){
            throw new RuntimeException("没有传递method参数,请给出你想调用的方法");
        }
        Class c = this.getClass();//获得当前类的Class对象
        Method method = null;
        try {
            //获得Method对象
            method =  c.getMethod(name, HttpServletRequest.class,HttpServletResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("没有找到"+name+"方法，请检查该方法是否存在");
        }

        try {
            method.invoke(this, req,resp);//反射调用方法
        } catch (Exception e) {
            System.out.println("你调用的方法"+name+",内部发生了异常");
            throw new RuntimeException(e);
        }

    }

    /**
     * 直接将传入的对象序列化为json，并且写回客户端
     * @param obj
     */
    public void writeValue(Object obj,HttpServletResponse res) throws IOException {
        res.setContentType("application/json;charset=utf-8");
        new ObjectMapper().writeValue(res.getOutputStream(),obj);
    }

    /**
     * 将传入的对象序列化为json，返回
     * @param obj
     * @return
     */
    public String writeValue(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

}
