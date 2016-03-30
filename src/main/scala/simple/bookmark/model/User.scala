package simple.bookmark.model

import org.joda.time.LocalDateTime

case class User(id: Long, name: String, createdAt: LocalDateTime, updatedAt: Option[LocalDateTime])
