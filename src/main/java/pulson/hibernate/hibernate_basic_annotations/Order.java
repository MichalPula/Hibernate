package pulson.hibernate.hibernate_basic_annotations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders", schema = "basic_annotations")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer money;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    Customer customer;

    public Order(Integer money) {
        this.money = money;
    }

    public Order(Integer money, Customer customer) {
        this.money = money;
        this.customer = customer;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("id=").append(id);
        sb.append(", money=").append(money);
        sb.append('}');
        return sb.toString();
    }
}
