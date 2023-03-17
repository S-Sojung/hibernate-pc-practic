package shop.mtcoding.hiberpc.model.user;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//JPA에서는 ToString 이 있으면 오류날 수 있음. 주의
@Setter // Dto가 있으면 얘는 필요없어진다.
@Getter
@NoArgsConstructor
@Table(name = "user_tb")
// entity로 등록하는 순간 하이버 네이트가 persistent context에서 관리하게 된다.
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public User(Integer id, String username, String password, String email, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdAt = createdAt;
    }

    public void update(String password, String email) {
        this.password = password;
        this.email = email;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email
                + ", createdAt=" + createdAt + "]";
    }

}
