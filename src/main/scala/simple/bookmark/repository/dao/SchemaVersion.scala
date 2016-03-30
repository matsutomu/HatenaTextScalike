package simple.bookmark.repository.dao

import scalikejdbc._
import org.joda.time.{DateTime}

case class SchemaVersion(
  versionRank: Int,
  installedRank: Int,
  version: String,
  description: String,
  `type`: String,
  script: String,
  checksum: Option[Int] = None,
  installedBy: String,
  installedOn: DateTime,
  executionTime: Int,
  success: Boolean) {

  def save()(implicit session: DBSession = SchemaVersion.autoSession): SchemaVersion = SchemaVersion.save(this)(session)

  def destroy()(implicit session: DBSession = SchemaVersion.autoSession): Unit = SchemaVersion.destroy(this)(session)

}


object SchemaVersion extends SQLSyntaxSupport[SchemaVersion] {

  override val tableName = "schema_version"

  override val columns = Seq("version_rank", "installed_rank", "version", "description", "type", "script", "checksum", "installed_by", "installed_on", "execution_time", "success")

  def apply(sv: SyntaxProvider[SchemaVersion])(rs: WrappedResultSet): SchemaVersion = apply(sv.resultName)(rs)
  def apply(sv: ResultName[SchemaVersion])(rs: WrappedResultSet): SchemaVersion = new SchemaVersion(
    versionRank = rs.get(sv.versionRank),
    installedRank = rs.get(sv.installedRank),
    version = rs.get(sv.version),
    description = rs.get(sv.description),
    `type` = rs.get(sv.`type`),
    script = rs.get(sv.script),
    checksum = rs.get(sv.checksum),
    installedBy = rs.get(sv.installedBy),
    installedOn = rs.get(sv.installedOn),
    executionTime = rs.get(sv.executionTime),
    success = rs.get(sv.success)
  )

  val sv = SchemaVersion.syntax("sv")

  override val autoSession = AutoSession

  def find(version: String)(implicit session: DBSession = autoSession): Option[SchemaVersion] = {
    withSQL {
      select.from(SchemaVersion as sv).where.eq(sv.version, version)
    }.map(SchemaVersion(sv.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[SchemaVersion] = {
    withSQL(select.from(SchemaVersion as sv)).map(SchemaVersion(sv.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(SchemaVersion as sv)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[SchemaVersion] = {
    withSQL {
      select.from(SchemaVersion as sv).where.append(where)
    }.map(SchemaVersion(sv.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[SchemaVersion] = {
    withSQL {
      select.from(SchemaVersion as sv).where.append(where)
    }.map(SchemaVersion(sv.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(SchemaVersion as sv).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    versionRank: Int,
    installedRank: Int,
    version: String,
    description: String,
    `type`: String,
    script: String,
    checksum: Option[Int] = None,
    installedBy: String,
    installedOn: DateTime,
    executionTime: Int,
    success: Boolean)(implicit session: DBSession = autoSession): SchemaVersion = {
    withSQL {
      insert.into(SchemaVersion).columns(
        column.versionRank,
        column.installedRank,
        column.version,
        column.description,
        column.`type`,
        column.script,
        column.checksum,
        column.installedBy,
        column.installedOn,
        column.executionTime,
        column.success
      ).values(
        versionRank,
        installedRank,
        version,
        description,
        `type`,
        script,
        checksum,
        installedBy,
        installedOn,
        executionTime,
        success
      )
    }.update.apply()

    SchemaVersion(
      versionRank = versionRank,
      installedRank = installedRank,
      version = version,
      description = description,
      `type` = `type`,
      script = script,
      checksum = checksum,
      installedBy = installedBy,
      installedOn = installedOn,
      executionTime = executionTime,
      success = success)
  }

  def batchInsert(entities: Seq[SchemaVersion])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'versionRank -> entity.versionRank,
        'installedRank -> entity.installedRank,
        'version -> entity.version,
        'description -> entity.description,
        'type -> entity.`type`,
        'script -> entity.script,
        'checksum -> entity.checksum,
        'installedBy -> entity.installedBy,
        'installedOn -> entity.installedOn,
        'executionTime -> entity.executionTime,
        'success -> entity.success))
        SQL("""insert into schema_version(
        version_rank,
        installed_rank,
        version,
        description,
        type,
        script,
        checksum,
        installed_by,
        installed_on,
        execution_time,
        success
      ) values (
        {versionRank},
        {installedRank},
        {version},
        {description},
        {type},
        {script},
        {checksum},
        {installedBy},
        {installedOn},
        {executionTime},
        {success}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: SchemaVersion)(implicit session: DBSession = autoSession): SchemaVersion = {
    withSQL {
      update(SchemaVersion).set(
        column.versionRank -> entity.versionRank,
        column.installedRank -> entity.installedRank,
        column.version -> entity.version,
        column.description -> entity.description,
        column.`type` -> entity.`type`,
        column.script -> entity.script,
        column.checksum -> entity.checksum,
        column.installedBy -> entity.installedBy,
        column.installedOn -> entity.installedOn,
        column.executionTime -> entity.executionTime,
        column.success -> entity.success
      ).where.eq(column.version, entity.version)
    }.update.apply()
    entity
  }

  def destroy(entity: SchemaVersion)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(SchemaVersion).where.eq(column.version, entity.version) }.update.apply()
  }

}
