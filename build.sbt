Settings.settings

libraryDependencies := Dependencies.all

SbtAliases.aliases.flatMap { case (alias, command) =>
  addCommandAlias(alias, command)
}

assembly / mainClass := Some(
  "com.codely.lesson_04_how_to_deploy_spark.video_01__deploy_application.DeploySparkApp"
)

import sbtassembly.MergeStrategy
assembly / assemblyMergeStrategy := {
  case PathList("org", "apache", "spark", "unused", "UnusedStubClass.class") => MergeStrategy.first
  case x => (assembly / assemblyMergeStrategy).value(x)
}
