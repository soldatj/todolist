# TodoList

## 과제

할일 목록(todo-list) 웹 어플리케이션 구현
[녹화영상](https://www.useloom.com/share/ce749126190b4ab59d463c79df7fc1a4 "녹화영상")
<!--녹화영상 수정-->

---

## 프로젝트 빌드, 실행 방법

### 1. 빌드 및 시작 
~~~
gradle build && java -jar build/libs/todolist-0.0.1.jar

※gradle, Lombok plugin 설치 상태에 주의
~~~
### 2. Swagger
~~~ 
http://localhost:8080/swagger
~~~
### 3. Root Page
~~~
http://localhost:8080/
~~~

---

## 요구 사항

* 사용자는 텍스트로 된 할일을 추가할 수 있다.
  * 할일 추가 시 다른 할일들을 참조 걸 수 있다.
  * 참조는 다른 할일의 id를 명시하는 형태로 구현한다.
* 사용자는 할일을 수정할 수 있다.
* 사용자는 할일 목록을 조회할 수 있다.
  * 조회 시 작성일, 최종수정일, 내용이 조회 가능하다.
  * 할일 목록은 페이징 기능이 있다.
* 사용자는 할일을 완료처리 할 수 있다.
  * 완료처리 시 참조가 걸린 완료되지 않은 할일이 있다면 완료 처리할 수 없다.

---

## 문제해결 전략

### 1. 할일 추가

* 사용자는 '새로입력' 버튼을 클릭해 모달창을 열고 사용자의 할일 컨텐츠와 참조할 일을 입력한다. 
  
  #### 1.1. 참조 번호 입력

  * 참조 번호는 자동완성 입력창을 이용해 사용자가 입력하는 사항을 받아들이고 선택사항에 대하여 쉼표로 구분하여 추가한다. 
  * 존재하지 않는 id를 입력한 경우 서버에서 id로 검색되지 않는 할일이 존재하는 경우 NotExistTodoException을 발생시키고 사용자에게 통보한다.

### 2. 할일 수정

* 사용자는 테이블의 id 컬럼을 선택하여 모달창을 열어 기존 입력된 할일을 수정 가능하다.

### 3. 할일 조회

* 할일 목록은 최초 접속시와 데이터가 입력, 수정, 완료, 완료취소가 되었을 때 자동 조회된다. 
  * ID, 할일, 작성일시, 최종수정일시, 완료여부가 조회 가능하다.
* 페이징 기능의 구현은 Web Side는 Datatables, Server Side는 Spring Data JPA의 페이징 기능을 이용하였다.

### 4. 할일 완료

* 사용자는 그리드의 할일 컬럼에 있는 버튼을 통해서 할일의 완료, 취소가 가능하다.
  * 할일을 완료 처리하는 경우 참조되고 있는 할일을 조회하여 미완료된 할일이 존재하면 사용자에게 통보하고 완료 처리하지 않는다.
  * 완료처리를 취소하는 경우 자신을 참조하고 있는 할일을 조회하여 완료된 할일이 존재하면 사용자에게 통보하고 완료 취소 처리하지 않는다.

---

## Dependencies
- Java8
- spring-boot
- starter-data-jpa
- spring-boot-starter-jdbc
- spring-boot-starter-thymeleaf
- spring-boot-starter-web
- swagger2
- swagger-ui
- jquery
- jquery-ui
- bootstrap
- lombok
- h2

---

## REST-API (http://localhost:8080/swagger)

### 1. find : Todo 조회
~~~
Method : GET 
URL : /api/todo/find/{id}
~~~
### 2. findAll : Todolist 조회
~~~
Method : GET 
URL : /api/todo
~~~
### 3. register : Todo 입력
~~~
Method : POST
URL : /api/todo
~~~
### 4. cancel : Todo 완료 취소
~~~
Method : PUT
URL : /api/todo/cancel/{id}
~~~
### 5. complete : Todo 완료
~~~
Method : PUT
URL : /api/todo/complete/{id}
~~~
### 6. findByIdNotAndContentLike : autocomplete용 Todolist 조회
~~~
Method : GET
URL : /api/todo/findByIdNotAndContentLike
~~~
### 7. modifyModal : 전달된 Todo의 일부를 수정(content, refTodoMapList)
~~~
Method : PUT
URL : /api/todo/modal/
~~~

---

## 개선 필요 사항

- [ ] 쌍방간 참조시 경고나 차단하는 기능 추가
