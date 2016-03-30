package simple.bookmark.model

import org.joda.time.LocalDateTime

case class Entry(id: Long, url: String, title: Option[String], createdAt: LocalDateTime, updatedAt: Option[LocalDateTime])
