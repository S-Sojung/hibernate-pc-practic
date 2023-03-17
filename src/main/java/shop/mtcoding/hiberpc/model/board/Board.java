package shop.mtcoding.hiberpc.model.board;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.mtcoding.hiberpc.model.user.User;

@Setter // Dto가 있으면 얘는 필요없어진다.
@Getter
@NoArgsConstructor
@Table(name = "board_tb")
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 만약 쌍방으로 참조를 하면????? 왔따 갔따 하면서 터질 수 있음 주의

    @ManyToOne
    // (Many : board / One : user) //최초 select 시 연관된 객체를 모두 다 들고옴 :EAGER
    // 연관된 것들을 getter 할 때 들고옴 !
    // 애초의 목적이 DTO가 없어도 잘 뽑아져 나옴 ...! 하지만 필요없는 데이터를 걸러내기 사용 .
    // @ManyToOne(fetch = FetchType.LAZY) // 지연로딩...
    private User user;
    // 자바세상에서는 객체로 만들수 잇고 테이블은 int로 만들어야한다...
    // 하이버네이트가 포린키로 연결해줄것이다.

    private String title;
    private String content;

    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public Board(Integer id, User user, String title, String content, Timestamp createdAt) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Board [id=" + id + ", user=" + user + ", title=" + title + ", content=" + content + ", createdAt="
                + createdAt + "]";
    }

}
