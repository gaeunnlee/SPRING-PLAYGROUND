package org.crud_ex.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.crud_ex.domain.MemberVO;

@Mapper
public interface MemberMapper {
    List<MemberVO> findAll();
    MemberVO findByUserno(int userno);
    int insert(MemberVO member);
    int update(MemberVO member);
    int delete(int userno);
}
