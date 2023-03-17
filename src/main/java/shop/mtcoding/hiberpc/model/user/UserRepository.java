package shop.mtcoding.hiberpc.model.user;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UserRepository {
    private final EntityManager em;

    public User findById(int id) {
        return em.find(User.class, id);
    }

    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }

    public User save(User user) {
        // primary Key가 있으면 merge, 없으면 save
        if (user.getId() == null) {
            em.persist(user);
        } else {// 업데이트 할땐 값만 바꾸고 호출도 안할 예정임. 더티체킹함!!!
            // 만약 업데이트 한다면 영속화 된 것을 들고와야한다 ...
            User userPS = em.find(User.class, user.getId());
            // 하지만 없는 id를 들고와버린다면??? 그런건 제어 해줘야한다. 검증해서.
            if (userPS != null) {
                em.merge(user);
            }
            System.out.println("잘못된 머지");
        }
        return user;
    }// update는 save와 같이

    public void delete(User user) {
        em.remove(user);
    }

}
