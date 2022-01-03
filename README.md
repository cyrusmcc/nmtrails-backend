# NMTrails 

[Link to live site here](https://www.nmtrails.com/)

Perhaps the greatest source of recreation in New Mexico comes through our expansive access to the outdoors and the adventures that come along with it. Hiking is one of the best ways in which we can engage and explore with our surrounding environments.  NMTrails aims to provide a useful state-focused resource for the local & tourist community of outdoor enthusiasts through offering a platform which allows for finding and researching interesting hiking trails in the various parks and regions in New Mexico. Through the use of open data, we can provide users with access to over 900 hiking trails spanning 21 different regions here in New Mexico. Users can browse and search through trails, learn about recent nature sightings on each trail, add trails to hiking wish-lists as well as leave ratings on the trails they've completed.


## About us

NMTrails was built by Chris Medlin & Cyrus McCormick for the 2021-2022 UNM App Contest. At the time of this app's creation, we are both studying Computer Science at the University of New Mexico. We first met while attending CNM where we worked together on a project for our intermediate programming class, making NMTrails the second full stack project we've worked on together as a team. We are both passionate about the field of development and computer science and wanted to apply ourselves towards building a web app that would act as a resource for our surrounding community.

## Design

### **Backend** [Link to repository](https://github.com/cyrusmcc/nmtrails-backend)

>[API Documentation](https://nmtrails-api-docs.readthedocs.io/en/latest/)
>
> NMTrails server is built utilizing Java, Spring Boot, and PostreSQL. Project architecture follows the Spring MVC pattern and utilizes a REST API. Data is mapped using Spring's  implementation of JPA and a python script is used to initialize the database with open data provided by NFS & NPS. Each trail's geo data entries are represented as lists of segments which contain collections of coordinates in GeoJSON format. Trail images are represented through images of their respective region and are provided by Wikicommons. Security is handled with the Spring Security module and utilizes token based authentication through JWT access tokens and refresh tokens. Passwords are hashed with Spring's BCrypt password encoder prior to storage to ensure that each user's information is secure. User's can request resets for lost passwords, password changes, and email changes. These reset requests are handeled with single-use JWT embedded URLs that are sent to user's emails through JavaMail.



### **Frontend** [Link to repository](https://github.com/cyrusmcc/nmtrails-frontend)

>NMTrails frontend is a SPA built with the Vue.js framework and approached with a mobile-first design. User data entry is verified using form validation provided by [VeeValidate](https://github.com/logaretm/vee-validate) and extends [Yup](https://github.com/jquense/yup) for value parsing and object schema validation. Queries to our backend API are handled through Axios and routing is done with the Vue Router dependency. The [Naive-UI](https://github.com/TuSimple/naive-ui) component library is used for some of our components such as the home screen's image carosel as well as star ratings. A Vuex store is utilized for managing a user's data when logged in.

## Showcase

https://user-images.githubusercontent.com/61042997/147840327-c4be9332-6b42-4a50-bdcd-27f7c8a288e3.mp4


[Backup youtube video](https://www.youtube.com/watch?v=tbKLa_n_eaM)

![pretty1](https://user-images.githubusercontent.com/61042997/147841366-d9bb4957-27f6-417b-986b-7204171186bb.png)

![pretty2](https://user-images.githubusercontent.com/61042997/147839287-123d0fef-35bc-4ab2-90d2-3b23fa6e2b71.png)


## Build instructions

### Backend
**Prerequisites to build**

You must ensure that you have Java & Maven installed & their respective environment paths set up in order to build the project. You must also have PostgreSQL & the PostGIS plugin installed to initialize the database.

**Project setup**
>In order to initialize the database with our open data sources you must run the dbinit.py script found in the dbinit directory. This can be done by running python dbinit.py "yourDBusername" "yourDBPassword". The required dependencies to run the script can be found in the requirements text file & installed with pip.

**Compiles and hot-reloads for development**

**Compile & run project for development**
```
mvn spring-boot:run, or utilize Intellij's run feature
```

**Compile for production**
```
mvn install - Environment variables must be set, the easiest way to do this for testing purposes is through modifying the command as follows, 
mvn -DDB_HOST=localhost -DDB_PORT=5432 -DDB_USER=yourbdusername -DDB_PASS=yourdbpassword -DJWT_SECRET=NA -DEMAIL_HOST="NA" -DEMAIL_PORT=0 -DGOOGLE_API_KEY=NA -DGOOGLE_ENGINE_ID=NA -DEMAIL_USER="NA" -DEMAIL_PASSWORD=NA install
```

### Frontend
**Prerequisites to build**

You must ensure that you have node.js and npm installed prior to building the project.
The versions used for development (and thus can be verified as compatible) are
- Node V14.15.5
- Npm V6.14.11

**Project setup**
```
npm install
```

**Compiles and hot-reloads for development**
```
npm run serve
```

**Compiles and minifies for production**
```
npm run build
```


## Extra credit
[Link to Adobe XD prototype presentation](https://xd.adobe.com/view/438468a9-484e-4074-8e07-e363268aa01b-16d7/?fullscreen&hints=off)

**Open Data Sources**

[USFS Geospatial Dataset](https://data-usfs.hub.arcgis.com/datasets/national-forest-system-trails-feature-layer/explore?location=34.853834%2C-105.069815%2C6.32)

[NPS Open Trail Dataset](https://public-nps.opendata.arcgis.com/search?collection=Dataset&q=trail)

[iNaturalist Observation API](https://api.inaturalist.org/v1/docs/)
## License
[MIT](https://choosealicense.com/licenses/mit/)
