import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "medications"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "securesocial" % "securesocial_2.9.1" % "2.0.8",
      "postgresql" % "postgresql" % "8.4-702.jdbc4"
      // Add your project dependencies here,
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      resolvers += Resolver.url("SecureSocial Repository", url("http://securesocial.ws/repository/snapshots/"))(Resolver.ivyStylePatterns),
      resolvers += Resolver.file("Local Repository", file("file://D:/play-2.0.4/repository/local/"))(Resolver.ivyStylePatterns)   
      // Add your own project settings here      
    )

}
