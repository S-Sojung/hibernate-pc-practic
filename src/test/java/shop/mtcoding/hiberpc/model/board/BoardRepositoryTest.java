package shop.mtcoding.hiberpc.model.board;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import shop.mtcoding.hiberpc.config.dummy.MyDummyEntity;
import shop.mtcoding.hiberpc.model.user.User;
import shop.mtcoding.hiberpc.model.user.UserRepository;

@Import({ BoardRepository.class, UserRepository.class })
@DataJpaTest
public class BoardRepositoryTest extends MyDummyEntity {
    // test는 무조건 autowired 해야한다.
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        // 근데? 롤백을 한다고 auto incresment 가 초기화 되지않아서 ... 여기서 초기화 해준다.
        // NativeQuery 하는거... 별로 안좋음.. sql 마다 쿼리가 약간씩 다르단 말.
        em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE board_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();

    }

    @Test
    public void save_test() {
        // given 1
        User user = newUser("ssar");
        User userPS = userRepository.save(user);

        // given 2
        Board board = newBoard("제목1", userPS);

        // when
        Board boardPS = boardRepository.save(board);
        System.out.println("테스트 : " + boardPS);

        // then
        assertThat(boardPS.getId()).isEqualTo(1);
        assertThat(boardPS.getUser().getId()).isEqualTo(1);
    }

    @Test
    public void update_test() {
        // given1 -db에 영속화
        User user = newUser("ssar");
        User userPS = userRepository.save(user);
        Board board = newBoard("제목1", userPS);
        Board boardPS = boardRepository.save(board);

        // given2 -request 데이터 생성
        String title = "제목12";
        String content = "내용12";

        // when
        boardPS.update(title, content);
        em.flush();
        // 트랜젝션 종료시 자동 발동되는 flush . 굳이 save 할 필요 없다.

        // 하지만 우리는 할 수 있다.
        // then
        Board findBoardPS = boardRepository.findById(1);

        assertThat(findBoardPS.getId()).isEqualTo(1);
        assertThat(findBoardPS.getTitle()).isEqualTo("제목12");
        assertThat(findBoardPS.getContent()).isEqualTo(content);
    }

    @Test
    public void delete_test() {
        // given1 -db에 영속화
        User user = newUser("ssar");
        User userPS = userRepository.save(user);
        Board board = newBoard("제목1", userPS);
        Board boardPS = boardRepository.save(board);

        // given -request데이터
        // em.clear(); // 테스트 용 (쿼리가 가는걸 보기 위해.) : 이걸 해야 쿼리를 볼 수 있는 이유: 아래. 캐싱.
        int id = 1;
        Board findBoardPS = boardRepository.findById(id); // pc에 존재하기 때문에 쿼리 안날라가고 캐싱함 .
        System.out.println(findBoardPS); // lazy 되어있는데 왜 다들고오냐... toString 에서 user를 부름

        // when
        boardRepository.delete(findBoardPS);

        // then
        Board deleteBoardPS = boardRepository.findById(id);
        assertThat(deleteBoardPS).isNull();
    }

    @Test
    public void findById_test() {
        // given1 -db에 영속화
        User user = newUser("ssar");
        User userPS = userRepository.save(user);
        Board board = newBoard("제목1", userPS);
        boardRepository.save(board);

        // given 2
        int id = 1;

        // when
        Board boardPS = boardRepository.findById(id);

        // then
        assertThat(boardPS.getTitle()).isEqualTo("제목1");
        assertThat(boardPS.getUser().getUsername()).isEqualTo("ssar");
    }

    @Test
    public void findAll_test() {
        // given1 -db에 영속화
        User user = newUser("ssar");
        User userPS = userRepository.save(user);
        List<Board> boardList = Arrays.asList(newBoard("제목1", userPS), newBoard("제목2", userPS));

        // stream -> os 단위의 object로 변환한다.
        boardList.stream().forEach((board) -> {
            boardRepository.save(board);
        });

        // when
        List<Board> boardListPS = boardRepository.findAll();

        // then
        assertThat(boardListPS.size()).isEqualTo(2);
    }
}
