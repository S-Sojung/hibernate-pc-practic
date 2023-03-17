package shop.mtcoding.hiberpc.config.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

//Web.xml에 필터 넣어줘서 걸러주면 되는데 그러면 DB 접근을 못한다. 근데 DB 연결은 추천은 안함 
//DB 연결하려면 IoC 컨테이너에 띄우고, DB 연결 안해도 되면 그냥 new 해서 써도 된다. 
public class MyBlackListFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 여기서 버퍼를 보고 버퍼를 비워버리면 안됨 !!!
        // 버퍼를 비우면 컨트롤러에서 버퍼를 읽지 못한다 !!! 주의 !!!
        // 보통 이런걸 필터에서 검증하지 말고 컨트롤러에서 검증한다.

        // 데이터 타입 : x-www-form-urlencoded

        String value = request.getParameter("value");

        if (value == null) {
            response.setContentType("text/plain; charset=utf-8");
            response.getWriter().println("value 파라메터를 전송해주세요.");
            return;
        }

        if (value.equals("babo")) {
            response.setContentType("text/plain; charset=utf-8");
            response.getWriter().println("당신은 블랙리스트가 되었습니다.");
            return;
        }
        // DS를 타야지 한글이 깨지지 않고 잘 넘어간다.
        chain.doFilter(request, response);
        // 정상적이면 계속 들어가면 된다. (혹은 다음필터)
    }
}