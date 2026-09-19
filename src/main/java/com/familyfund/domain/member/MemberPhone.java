package com.familyfund.domain.member;

import jakarta.persistence.*;

@Entity
@Table(name = "member_phone", uniqueConstraints = {@UniqueConstraint(name = "uk_member_phone_number", columnNames = {"member_id", "phone_number"})}, indexes = {@Index(name = "idx_member_phone_number", columnList = "phone_number")})
public class MemberPhone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_member_phone_member"))
    private Member member;

    @Column(name = "phone_number", nullable = false, length = 11)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "phone_type", nullable = false, length = 20)
    private PhoneType phoneType;

    @Column(name = "is_primary", nullable = false)
    private boolean primary;

    @Column(name = "description", length = 500)
    private String description;

    protected MemberPhone() {
    }

    public MemberPhone(String phoneNumber, PhoneType phoneType, boolean primary) {
        this.phoneNumber = phoneNumber;
        this.phoneType = phoneType;
        this.primary = primary;
    }

    public MemberPhone(String phoneNumber, PhoneType phoneType, boolean primary, String description) {
        this.phoneNumber = phoneNumber;
        this.phoneType = phoneType;
        this.primary = primary;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "MemberPhone{" + "id=" + id + ", member=" + member + ", phoneNumber='" + phoneNumber + '\'' + ", phoneType=" + phoneType + ", primary=" + primary + ", description='" + description + '\'' + '}';
    }
}