package %packageName%.dao.base;

import java.util.List;

/**
 * 공통 검색조건
 */
public class SearchParameter {
  private SearchParameter parentScParam;
  private Integer searchType;
  private Integer searchType2;
  private Integer searchType3;
  private Integer searchType4;

  private String searchValue;
  private String searchValue2;
  private String searchValue3;
  private String searchValue4;
  private String searchValue5;
  private String searchValue6;
  private String searchValue7;

  private String sdate;
  private String edate;
  private String sdate2;
  private String edate2;
  private String sdate3;
  private String edate3;

  private int page;
  private int size;

  private List<String> searchList;

  public Integer getSearchType() {
    return searchType;
  }

  public void setSearchType(Integer searchType) {
    this.searchType = searchType;
  }

  public String getSearchValue() {
    return searchValue;
  }

  public void setSearchValue(String searchValue) {
    this.searchValue = searchValue;
  }

  public Integer getSearchType2() {
    return searchType2;
  }

  public void setSearchType2(Integer searchType2) {
    this.searchType2 = searchType2;
  }

  public String getSearchValue2() {
    return searchValue2;
  }

  public void setSearchValue2(String searchValue2) {
    this.searchValue2 = searchValue2;
  }

  public String getSearchValue3() {
    return searchValue3;
  }

  public void setSearchValue3(String searchValue3) {
    this.searchValue3 = searchValue3;
  }

  public String getSdate() {
    return sdate;
  }

  public void setSdate(String sdate) {
    this.sdate = sdate;
  }

  public String getEdate() {
    return edate;
  }

  public void setEdate(String edate) {
    this.edate = edate;
  }

  public Integer getSearchType3() {
    return searchType3;
  }

  public void setSearchType3(Integer searchType3) {
    this.searchType3 = searchType3;
  }

  public Integer getSearchType4() {
    return searchType4;
  }

  public void setSearchType4(Integer searchType4) {
    this.searchType4 = searchType4;
  }

  public String getSdate2() {
    return sdate2;
  }

  public void setSdate2(String sdate2) {
    this.sdate2 = sdate2;
  }

  public String getEdate2() {
    return edate2;
  }

  public void setEdate2(String edate2) {
    this.edate2 = edate2;
  }

  public String getSearchValue4() {
    return searchValue4;
  }

  public void setSearchValue4(String searchValue4) {
    this.searchValue4 = searchValue4;
  }


  public List<String> getSearchList() {
    return searchList;
  }

  public void setSearchList(List<String> searchList) {
    this.searchList = searchList;
  }

  public String getSearchValue5() {
    return searchValue5;
  }

  public void setSearchValue5(String searchValue5) {
    this.searchValue5 = searchValue5;
  }

  public String getSearchValue6() {
    return searchValue6;
  }

  public void setSearchValue6(String searchValue6) {
    this.searchValue6 = searchValue6;
  }

  public String getSearchValue7() {
    return searchValue7;
  }

  public void setSearchValue7(String searchValue7) {
    this.searchValue7 = searchValue7;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public String getSdate3() {
    return sdate3;
  }

  public void setSdate3(String sdate3) {
    this.sdate3 = sdate3;
  }

  public String getEdate3() {
    return edate3;
  }

  public void setEdate3(String edate3) {
    this.edate3 = edate3;
  }

  public SearchParameter getParentScParam() {
    return parentScParam;
  }

  public void setParentScParam(SearchParameter parentScParam) {
    this.parentScParam = parentScParam;
  }
}
