# generate-mybatis-source
DB에서 테이블 및 컬럼 정보를 조회한 데이터를 기반으로 Entity, Dao 객체 및 Sql 쿼리 xml을 생성해주는 프로젝트.

### [실행전 필요한 작업]
src/main/resources/config.properites 설정하기

    #기본 패키지명
    #기본 패키지아래 dao, entity 에 각 java파일들 생성됨
    #예) com.naver.dao, com.naver.entity 
    package.base=com.naver
    
    #테이블 접두사가 있어서 객체에서는 제외하고보고 싶다면 아래 항목 설정.
    #예) TB_BOARD 테이블을 Board.java 로 보고 싶다면 TB_를 설정 (콤마를 이용해서 여러개 설정가능)
    #필요없다면 공백
    remove.prefix.table_name=
    
    #지정된 접두사로 시작되는 테이블만 처리
    #예) TB_MEMBER_
    include.prefix.table_name=
    
    #DB JDBC URL을 Vendor에 맞게 작성.
    #db.url=jdbc:mysql://IP:3306/DBNAME?characterEncoding=utf8&autoReconnect=true
    #db.url=jdbc:mariadb://IP:3306/DBNAME?characterEncoding=utf8&autoReconnect=true
    #db.url=jdbc:sqlserver://IP:1433;DatabaseName=DBNAME
    #db.url=jdbc:cubrid:IP:33000:DBNAME:::
    #db.url=jdbc:oracle:thin:@IP:1521:DBNAME
    #db.url=jdbc:db2://IP:50000/DBNAME
    
    #DB 접속 아이디
    #db.id=
    
    #DB 접속 비번
    #db.pwd=
    
### [실행파일]
com.maumjido.generate.mybatis.source.Main 실행.

### [실행결과]
프로젝트 폴더에 result 폴더가 생성되고 그 하위에 결과가 생성됨.
- 테이블별 Entity파일
- 테이블별 Dao파일
- mybatis-config.xml 파일
- 테이블별 sql.xml 파일

### [결과샘플]
 
## Holiday 테이블
 
## Entity:Holiday.java

    import java.util.Date;
        
    /**
     * 공휴일 Class
     */
    public class Holiday {
      private String holiDate;// 공휴일(형식:yyyymmdd)
      private String holiName;// 공휴일명
      private String regUid;// 등록자id
      private Date regDate;// 등록일시
      private String modUid;// 최종수정자id
      private Date modDate;// 최종수정일시
    
      /**
       * 공휴일(형식:yyyymmdd) 조회
       * 
       * @return holiDate
       */
      public String getHoliDate() {
        return this.holiDate;
      }
    
      /**
       * 공휴일(형식:yyyymmdd) 설정
       * 
       * @return holiDate
       */
      public void setHoliDate(String holiDate) {
        this.holiDate = holiDate;
      }
    
      /**
       * 공휴일명 조회
       * 
       * @return holiName
       */
      public String getHoliName() {
        return this.holiName;
      }
    
      /**
       * 공휴일명 설정
       * 
       * @return holiName
       */
      public void setHoliName(String holiName) {
        this.holiName = holiName;
      }
    
      /**
       * 등록자id 조회
       * 
       * @return regUid
       */
      public String getRegUid() {
        return this.regUid;
      }
    
      /**
       * 등록자id 설정
       * 
       * @return regUid
       */
      public void setRegUid(String regUid) {
        this.regUid = regUid;
      }
    
      /**
       * 등록일시 조회
       * 
       * @return regDate
       */
      public Date getRegDate() {
        return this.regDate;
      }
    
      /**
       * 등록일시 설정
       * 
       * @return regDate
       */
      public void setRegDate(Date regDate) {
        this.regDate = regDate;
      }
    
      /**
       * 최종수정자id 조회
       * 
       * @return modUid
       */
      public String getModUid() {
        return this.modUid;
      }
    
      /**
       * 최종수정자id 설정
       * 
       * @return modUid
       */
      public void setModUid(String modUid) {
        this.modUid = modUid;
      }
    
      /**
       * 최종수정일시 조회
       * 
       * @return modDate
       */
      public Date getModDate() {
        return this.modDate;
      }
    
      /**
       * 최종수정일시 설정
       * 
       * @return modDate
       */
      public void setModDate(Date modDate) {
        this.modDate = modDate;
      }
    }

## Dao:HolidayDao.java ( [BaseDao](https://github.com/goni9071/generate-mybatis-source/blob/master/src/main/resources/template/BaseDao.java) 에 기본 CRUD가 포함되어 있음)

    import org.apache.ibatis.annotations.Mapper;
    import kr.co.gampartners.mobile.dao.base.BaseDao;
    import kr.co.gampartners.mobile.entity.Holiday;
    
    @Mapper
    public interface HolidayDao extends BaseDao<Holiday, String> {
    }
    
## Sql:Holiday.xml
    
    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
    <mapper namespace="기본패키지.dao.HolidayDao">
        <resultMap type="Holiday" id="HolidayRM">
            <id property="holiDate" column="holi_date" javaType="String"></id><!--공휴일(형식:YYYYMMDD)-->
            <result property="holiName" column="holi_name" javaType="String"></result><!--공휴일명-->
            <result property="regUid" column="reg_uid" javaType="String"></result><!--등록자ID-->
            <result property="regDate" column="reg_date" javaType="Date"></result><!--등록일시-->
            <result property="modUid" column="mod_uid" javaType="String"></result><!--최종수정자ID-->
            <result property="modDate" column="mod_date" javaType="Date"></result><!--최종수정일시-->
        </resultMap>
        <insert id="insert" parameterType="Holiday">
            /* 공휴일 등록 */
            INSERT INTO erp_holiday (
                   holi_date,
                   holi_name,
                   reg_uid,
                   reg_date,
                   mod_uid,
                   mod_date
                 ) VALUES (
                   #{entity.holiDate},
                   #{entity.holiName},
                   #{entity.regUid},
                   #{entity.regDate},
                   #{entity.modUid},
                   #{entity.modDate})
        </insert>
        <update id="update" parameterType="Holiday">
            /* 공휴일 수정 */
            UPDATE erp_holiday 
                   <set>
                   <if test="entity.holiName != null">
                   holi_name = #{entity.holiName},
                   </if>
                   <if test="entity.regUid != null">
                   reg_uid = #{entity.regUid},
                   </if>
                   <if test="entity.regDate != null">
                   reg_date = #{entity.regDate},
                   </if>
                   <if test="entity.modUid != null">
                   mod_uid = #{entity.modUid},
                   </if>
                   <if test="entity.modDate != null">
                   mod_date = #{entity.modDate},
                   </if>
                   </set>
             WHERE holi_date = #{entity.holiDate}
        </update>
        <delete id="delete" parameterType="Holiday">
            /* 공휴일 삭제 */
            DELETE FROM erp_holiday 
             WHERE holi_date = #{holiDate}
        </delete>
        <select id="selectOne" resultMap="HolidayRM">
            /* 공휴일 상세조회 */
            SELECT holi_date,
                   holi_name,
                   reg_uid,
                   reg_date,
                   mod_uid,
                   mod_date
              FROM erp_holiday
             WHERE holi_date = #{holiDate}
        </select>
        <select id="selectList" resultMap="HolidayRM">
            /* 공휴일 목록조회 */
            SELECT holi_date,
                   holi_name,
                   reg_uid,
                   reg_date,
                   mod_uid,
                   mod_date
              FROM erp_holiday
                   <if test="pageable != null">
             WHERE LIMIT #{pageable.start}, #{pageable.end}
                   </if>
        </select>
        <select id="selectListCount" resultType="int">
            /* 공휴일 전체 개수 조회 */
            SELECT COUNT(*) AS CNT
              FROM erp_holiday
             WHERE holi_date = #{holiDate}
        </select>
    </mapper>

