import java.time.LocalDateTime
import java.time.format.{DateTimeFormatterBuilder, SignStyle}
import java.time.temporal.ChronoField._

import com.typesafe.config._
import dispatch.Defaults._
import dispatch._
import org.json4s.{JInt, JObject, _}

object GithubPullRequest extends App {

  // parser for LocalDateTime
  val parse2DT: (String => LocalDateTime) = s => {
    LocalDateTime.parse(s,
      new DateTimeFormatterBuilder().parseCaseInsensitive
        .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
        .appendLiteral('-')
        .appendValue(MONTH_OF_YEAR, 2)
        .appendLiteral('-')
        .appendValue(DAY_OF_MONTH, 2)
        .appendLiteral('T')
        .appendValue(HOUR_OF_DAY, 2)
        .appendLiteral(':')
        .appendValue(MINUTE_OF_HOUR, 2)
        .optionalStart()
        .appendLiteral(':')
        .appendValue(SECOND_OF_MINUTE, 2)
        .appendLiteral('Z')
        .toFormatter())
  }

  // get token from configuration file
  val cnf = ConfigFactory.load()
  val token = cnf.getString("github.token")

  val headers = Map("Authorization" -> ("token " + token))

  val svc = url("https://api.github.com/repos/doilux/github-prac-and-dev/pulls?state=close") <:< headers
  val res = Http(svc OK as.json4s.Json)

  val prs = for {
    JObject(pr) <- res()
    JField("user", JObject(usr)) <- pr

    JField("number", JInt(number)) <- pr
    JField("login", JString(owner)) <- usr
    JField("created_at", JString(createdAt)) <- pr
    JField("merged_at", JString(mergedAt)) <- pr

    // どうやらmergedAtがNullの場合はこのループから除外されるらしいので、mergedAtがNoneのことはないのだがまあいいか
    pulls = new PullRequest(number, owner, parse2DT(createdAt), Option(mergedAt).map(s => parse2DT(s)))
  } yield pulls

  // レポートを出力する（todo : そーとする）
  PRUtil.report(prs, n => n.closedAt.get.toLocalDate.withDayOfMonth(1))

  // ユーザー別
  PRUtil.report(prs, n => n.owner)
}





