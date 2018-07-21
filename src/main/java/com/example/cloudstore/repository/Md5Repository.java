package com.example.cloudstore.repository;

import com.example.cloudstore.domain.Md5;
import com.sun.jmx.snmp.mpm.SnmpMsgTranslator;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author jitdc
 * @Date Create in 16:58 2018/7/6
 * @Description:
 */
@Aspect
public interface Md5Repository extends JpaRepository<Md5,Integer> {
    Md5 save(Md5 md5);

    List<Md5> findByFileMd5AndFileName(String fileMd5, String fileName);
    Md5 findByFileMd5AndPathAndFileName (String fileMd5, String path,String fileName);
    List<Md5> findByFileMd5AndPath(String fileMd5,String path);
    List<Md5> findByFileMd5(String fileMd5);
    Md5 findByFileNameAndPath (String filename,String path);
    int  deleteByPathAndFileName (String path ,String filename);
    Md5 findByUidAndPathAndFileName (String uid,String path,String filename);

    List<Md5> findByFileMd5AndFileNameAndUsername (String fileMd5, String fileName,String username);

    @Query(value = "select u from Md5 u where u.path like ?1%")
    List<Md5> findByPathLike(@Param("path") String path);

    @Modifying
    @Transactional
    @Query("update Md5 u set u.path =?1  where u.uid = ?2 and u.path = ?3 and u.fileName = ?4")
    int updateDirPath(@Param("path") String newPath,@Param("uid") String uid,@Param("path") String oldPath,@Param("fileName") String fileName);

    @Modifying
    @Transactional
    @Query("update Md5 u set u.fileName =?1  where u.uid = ?2 and u.path = ?3 and u.fileName = ?4")
    int updateDirName(@Param("fileName") String newFilename,@Param("uid") String uid,@Param("path") String path,@Param("fileName") String fileName);


    @Modifying
    @Transactional
    @Query("update Md5 u set u.fileName = ?1 where u.path = ?2 and u.fileName=?3")
    int updateFileName(@Param("filename") String newFilename,@Param("path") String path,@Param("filename") String oldFilename);

    @Modifying
    @Transactional
    @Query("update Md5 u set u.path = ?1 where u.path = ?2 and u.fileName=?3")
    int updateFilePath(@Param("path") String newPath,@Param("path") String oldPath,@Param("filename") String filename);

    @Modifying
    @Transactional
    @Query("update Md5 u set u.path = ?1 where u.path = ?2")
    int updateFilePathInDirNameLike(@Param("path") String newPath,@Param("path") String oldPath);




    int deleteByPath (String path);
    int deleteById (String id);
    int deleteByUidAndPathAndFileName(String uid, String path ,String filename);

    @Modifying
    @Query(value = "delete FROM Md5 u WHERE u.path LIKE ?1%")
    int  deleteByPathLike (@Param("path") String path);

}
