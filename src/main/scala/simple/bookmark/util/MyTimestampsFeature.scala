package simple.bookmark.util

import skinny.orm.feature.TimestampsFeature
import skinny.orm.feature.SoftDeleteWithTimestampFeature

trait MyTimestampsFeature[Entity] extends TimestampsFeature[Entity] {

  override def createdAtFieldName = "createdTimestamp"
  override def updatedAtFieldName = "updatedTimestamp"
}


trait MySoftDeleteWithTimestampFeature[Entity] extends SoftDeleteWithTimestampFeature[Entity] {
  override def deletedAtFieldName = "deletedTimestamp"
}