package simple.bookmark.service

/**
  * Created by tsutomu on 16/02/24.
  */
sealed trait Error {
  override def toString() = this match {
    case BookmarkNotFoundError => "Can not find a target bookmark."
    case EntryNotFoundError    => "Can not find a target entry."
  }
}

final case object BookmarkNotFoundError extends Error
final case object EntryNotFoundError extends Error
