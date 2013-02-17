import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "medications"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "securesocial" % "securesocial_2.9.1" % "master",
      "postgresql" % "postgresql" % "8.4-702.jdbc4"
      // Add your project dependencies here,
    ) 
    
    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings( 
      resolvers += Resolver.file("Local Repository", file("D:/play-2.0.4/repository/local/"))(Resolver.ivyStylePatterns)   
      // Add your own project settings here      
    )

}
