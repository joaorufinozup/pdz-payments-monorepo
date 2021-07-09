package pdz.payments.customer.models

import java.util.*
import javax.persistence.*

@Entity
@Table(name="customer")
data class Customer(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "name", nullable = false)
    val customerName: String,

    @Column(name = "email", nullable = false)
    val customerEmail: String,

    @Column(name = "phone", nullable = false)
    val customerPhone: String,

    @Column(name = "cpf", nullable = false, unique = true)
    val customerCpf: String,

    @Column(name = "birth_date", nullable = false)
    val customerBirthDate: String,

    @Column(name = "address", nullable = false)
    val customerAddress: String,

    )
