## 문제
아래 Bean 존재하지 않는 프로젝트 생성됨
```java
@org.springframework.beans.factory.annotation.Qualifier(value="vanillaRestTemplate")
```

## 해결
jhipster 버전 6.8.0 에서 버그 있음 
6.9.0 으로 업그레이드 후 실행