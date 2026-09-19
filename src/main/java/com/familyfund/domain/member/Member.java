package com.familyfund.domain.member;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member", uniqueConstraints = {@UniqueConstraint(name = "uk_member_member_code", columnNames = "member_code")}, indexes = {@Index(name = "idx_member_last_name", columnList = "last_name")})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_code", nullable = false, length = 50)
    private String memberCode;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "national_code", length = 20)
    private String nationalCode;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberPhone> phones = new ArrayList<>();

    protected Member() {
    }

    public Member(String memberCode, String firstName, String lastName) {
        this.memberCode = memberCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<MemberPhone> getPhones() {
        return phones;
    }

    public void setPhones(List<MemberPhone> phones) {
        this.phones = phones;
    }

    public void addPhone(MemberPhone phone) {
        phones.add(phone);
        phone.setMember(this);
    }

    public void removePhone(MemberPhone phone) {
        phones.remove(phone);
        phone.setMember(null);
    }

    public void updateInformation(String firstName, String lastName, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Member{" + "id=" + id + ", memberCode='" + memberCode + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", nationalCode='" + nationalCode + '\'' + ", description='" + description + '\'' + ", createdAt=" + createdAt + ", active=" + active + ", phones=" + phones + '}';
    }
}