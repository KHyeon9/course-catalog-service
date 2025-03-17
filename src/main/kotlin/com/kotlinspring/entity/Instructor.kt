package com.kotlinspring.entity

import jakarta.persistence.*

@Entity
@Table(name = "instructors")
class Instructor(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int?,
    var name: String,
    @OneToMany(
        mappedBy = "instructor",
        cascade = [(CascadeType.ALL)], // 이 엔티티가 삭제되면 포함된 Course들이 모두 삭제
        orphanRemoval = true // 부모 객체와 연관된 자식 엔티티를 삭제시 DB에도 자동 삭제됨
    )
    var corses: MutableList<Course> = mutableListOf(),
)