# Sistema de Gestión de Productos

Aplicación web Java con JSP, Servlets, JDBC y Oracle Database 21c.  
Desplegable en Apache Tomcat 9 como archivo WAR.

---

## Requisitos previos

| Herramienta | Versión mínima | Verificar con |
|---|---|---|
| Java JDK | 17 | `java -version` |
| Maven | 3.6+ | `mvn -version` |
| Apache Tomcat | 9.x | — |
| Oracle Database | 21c | — |

---

## 1. Configurar la base de datos Oracle

Conectarse como administrador (`sysdba`) y ejecutar:

```sql
-- Conectar como DBA
sqlplus sys/TU_PASSWORD@//localhost:1521/XEPDB1 AS SYSDBA

-- Crear usuario/schema
CREATE USER walther IDENTIFIED BY prueba;
GRANT CONNECT, RESOURCE, CREATE TABLE, CREATE SEQUENCE TO walther;
ALTER USER walther QUOTA UNLIMITED ON USERS;
EXIT;
```

Luego conectar con el nuevo usuario y crear las tablas:

```bash
sqlplus walther/prueba@//localhost:1521/testdb @src/main/resources/init.sql
```

> **Nota:** Si tu servicio Oracle no es `testdb`, ajusta la URL en el siguiente paso.

---

## 2. Configurar la conexión JDBC

Editar el archivo `src/main/java/com/examen/dao/ConexionDB.java` y ajustar las 3 constantes:

```java
private static final String URL      = "jdbc:oracle:thin:@//localhost:1521/testdb";
private static final String USUARIO  = "walther";
private static final String PASSWORD = "prueba";
```

| Parámetro | Descripción |
|---|---|
| `localhost:1521` | Host y puerto de Oracle |
| `/testdb` | Nombre del servicio (Service Name) de tu BD |
| `walther` | Usuario de Oracle |
| `prueba` | Contraseña del usuario |

---

## 3. Compilar y generar el WAR

Desde la raíz del proyecto (donde está el `pom.xml`):

```bash
mvn clean package -DskipTests
```

Al terminar verás:

```
[INFO] BUILD SUCCESS
[INFO] Building war: .../target/examen-tecnico.war
```

El archivo WAR queda en:
```
target/examen-tecnico.war
```

---

## 4. Copiar el driver Oracle a Tomcat

El driver JDBC de Oracle **debe estar en la carpeta `lib/` de Tomcat**, no solo dentro del WAR:

```bash
cp ~/.m2/repository/com/oracle/database/jdbc/ojdbc11/21.9.0.0/ojdbc11-21.9.0.0.jar \
   $TOMCAT_HOME/lib/
```

> Reemplaza `$TOMCAT_HOME` con la ruta real de tu Tomcat, por ejemplo `/opt/tomcat`.

---

## 5. Desplegar el WAR en Tomcat

```bash
# Opción A: copiar con el mismo nombre (accede como /examen-tecnico/)
cp target/examen-tecnico.war $TOMCAT_HOME/webapps/

# Opción B: renombrar para tener una URL más corta (accede como /examen/)
cp target/examen-tecnico.war $TOMCAT_HOME/webapps/examen.war
```

---

## 6. Iniciar Tomcat

```bash
# Linux / Mac
$TOMCAT_HOME/bin/startup.sh

# Windows
%TOMCAT_HOME%\bin\startup.bat
```

Verificar que arrancó correctamente:

```bash
tail -f $TOMCAT_HOME/logs/catalina.out
```

Debes ver algo como:
```
INFO: Server startup in [XXXX] milliseconds
```

---

## 7. Abrir la aplicación

| Opción de despliegue | URL |
|---|---|
| WAR con nombre `examen-tecnico.war` | http://localhost:8080/examen-tecnico/ |
| WAR renombrado a `examen.war` | http://localhost:8080/examen/ |

### Credenciales de acceso

| Usuario | Contraseña | Rol |
|---|---|---|
| `admin` | `admin123` | ADMIN |
| `user` | `user123` | USER |

---

## 8. Detener Tomcat

```bash
# Linux / Mac
$TOMCAT_HOME/bin/shutdown.sh

# Windows
%TOMCAT_HOME%\bin\shutdown.bat
```

---

## Alternativa: ejecución rápida con Tomcat embebido

Sin necesidad de instalar Tomcat externamente, para desarrollo:

```bash
mvn tomcat7:run
```

Abre: **http://localhost:8080/examen/**

> La base de datos Oracle debe estar corriendo igualmente.

---

## Estructura del proyecto

```
src/
├── main/
│   ├── java/com/examen/
│   │   ├── dao/         ConexionDB.java, ProductoDAO.java
│   │   ├── filter/      AuthFilter.java
│   │   ├── model/       Producto.java
│   │   └── servlet/     LoginServlet.java, ProductoServlet.java
│   │                    ApiProductoServlet.java, LogoutServlet.java
│   │                    PreferenciasServlet.java
│   ├── resources/
│   │   └── init.sql     (DDL + datos iniciales Oracle)
│   └── webapp/
│       ├── WEB-INF/web.xml
│       ├── css/styles.css
│       ├── js/app.js
│       ├── index.jsp
│       ├── login.jsp
│       ├── productos/   listar.jsp, formulario.jsp, detalle.jsp
│       └── error/       404.jsp
pom.xml
```

---

## Solución de problemas frecuentes

| Error | Causa probable | Solución |
|---|---|---|
| `ClassNotFoundException: oracle.jdbc.OracleDriver` | El JAR del driver no está en `$TOMCAT_HOME/lib/` | Copiar `ojdbc11-21.9.0.0.jar` a `lib/` |
| `ORA-01017: invalid username/password` | Credenciales incorrectas en `ConexionDB.java` | Verificar usuario y contraseña Oracle |
| `ORA-12541: TNS:no listener` | Oracle no está corriendo o el puerto es incorrecto | Verificar que Oracle esté activo en el puerto 1521 |
| `404 Not Found` al abrir la app | El WAR no se desplegó o el nombre de la URL es incorrecto | Verificar el nombre del WAR en `webapps/` y la URL |
| `HTTP 500` al iniciar | Error en el código Java | Revisar `$TOMCAT_HOME/logs/catalina.out` |
