package shop.mtcoding.hiberpc.model.user;

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

@Import(UserRepository.class) // JPARepository를 상속받은 repository만 띄워주기 때문에 import로 받아옴.
@DataJpaTest // Repository - DB 에서 DB 관련된 것들을 다 띄워준다.
// DataJpaTest에 Transactional이 되어있다.
public class UserRepositoryTest extends MyDummyEntity {
    // test는 무조건 autowired 해야한다.
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        // 근데? 롤백을 한다고 auto incresment 가 초기화 되지않아서 ... 여기서 초기화 해준다.
        // NativeQuery 하는거... 별로 안좋음.. sql 마다 쿼리가 약간씩 다르단 말.
        em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();

    }

    @Test
    public void save_test() {
        // given
        User user = newUser("ssar");

        // when
        User userPS = userRepository.save(user);

        // then
        assertThat(userPS.getId()).isEqualTo(1);
    }

    @Test
    public void update_test() {
        // given1 -db에 영속화
        User user = newUser("ssar");
        User userPS = userRepository.save(user);
        // given2 -request 데이터 생성
        String password = "5678";
        String email = "ssar@gmail.com";

        // when
        userPS.update(password, email);
        User updateUserPS = userRepository.save(userPS);

        // 트랜잭션 종료시점에 더티체킹 되는데... 여기서는 검증 할 수 없음!

        // 하지만 우리는 할 수 있다.
        // then
        assertThat(updateUserPS.getId()).isEqualTo(1);
        assertThat(updateUserPS.getPassword()).isEqualTo("5678");
        assertThat(updateUserPS.getEmail()).isEqualTo(email);
    }

    @Test
    public void update_dutty_checking_test() {
        // given1 -db에 영속화
        User user = newUser("ssar");
        User userPS = userRepository.save(user);
        // given2 -request 데이터 생성
        String password = "5678";
        String email = "ssar@gmail.com";

        // when -flush 해서 DB에 강제 더티체킹함
        userPS.update(password, email);
        em.flush();
        // 트랜잭션 종료시점에 더티체킹 되는데... 여기서는 검증 할 수 없음!
        // 하지만 우리는 할 수 있다.

        // 참고, 여기서 조회쿼리가 날아가지 않는다.
        // pc에서 캐싱되서 들고온다
        // then
        User updateUserPS = userRepository.findById(1);
        assertThat(updateUserPS.getId()).isEqualTo(1);
        assertThat(updateUserPS.getPassword()).isEqualTo("5678");
        assertThat(updateUserPS.getEmail()).isEqualTo(email);
    }

    @Test
    public void delete_test() {
        // given1 -db에 영속화
        User user = newUser("ssar");
        userRepository.save(user);

        // given -request데이터
        int id = 1;
        User findUserPS = userRepository.findById(id); // pc에 존재하기 때문에 쿼리 안날라가고 캐싱함 .

        // when
        userRepository.delete(findUserPS);

        // then
        User deleteUserPS = userRepository.findById(1);
        assertThat(deleteUserPS).isNull();
    }

    @Test
    public void findById_test() {
        // given1 -db에 영속화
        User user = newUser("ssar");
        userRepository.save(user);

        // given 2
        int id = 1;

        // when
        User userPS = userRepository.findById(id);

        // then
        assertThat(userPS.getUsername()).isEqualTo("ssar");
    }

    @Test
    public void findAll_test() {
        // given1 -db에 영속화
        List<User> userList = Arrays.asList(newUser("ssar"), newUser("cos"));

        // stream -> os 단위의 object로 변환한다.
        userList.stream().forEach((user) -> {
            userRepository.save(user);
        });

        // when
        List<User> userListPS = userRepository.findAll();

        // then
        assertThat(userListPS.size()).isEqualTo(2);
    }
}
