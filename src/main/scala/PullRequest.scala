import java.time.{Duration, LocalDateTime}

case class PullRequest(
                        id: BigInt,
                        owner: String,
                        createdAt: LocalDateTime,
                        closedAt: Option[LocalDateTime],
                        duration: Option[Duration]
                      ) {
  def this(
            id: BigInt,
            owner: String,
            createdAt: LocalDateTime,
            closedAt: Option[LocalDateTime]
          ) = {
    this(id, owner, createdAt, closedAt, closedAt.map(Duration.between(createdAt, _)))
  }
}


object PRUtil {

  val avg: (List[Long] => Long) = xs => xs.sum / xs.length

  /**
    * レポートを出力する
    *
    * @param l PullRequestのリスト
    * @param f PullRequestをグルーピングするファンクション
    * @tparam K PullRequestのグルーピングに用いる型
    */
  def report[K](l: List[PullRequest], f: PullRequest => K) = {
    l.filter(_.closedAt.isDefined).groupBy(f).map {
      kv => Tuple2(kv._1, avg(kv._2.map(_.duration.get.getSeconds)))
    }.foreach(kv => println(kv._1 + "," + kv._2 / 60 + "min"))
  }
}