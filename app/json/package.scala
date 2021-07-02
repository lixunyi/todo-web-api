import play.api.libs.json._
import play.api.libs.json.Reads._

package object json {
  implicit class JsPathOps(self: JsPath) {
    // JsPath#readNullableがundefinedとnullを区別しないので、区別するメソッドを独自で定義
    // 読み込むJsPathの末尾より前のパスがなかったらJsError("error.path.missing")
    // 読み込むJsPathの末尾がundefinedならNone
    // 読み込むJsPathの値がnullならSome(None)
    // 読み込むJsPathに値が存在するならSome(Some(A))
    def readOptional[A](implicit reads: Reads[A]): Reads[Option[Option[A]]] = {
      Reads[Option[Option[A]]] { json =>
        self
          .applyTillLast(json)
          .fold(identity,
                _.fold(
                  _ => JsSuccess(None),
                  _ match {
                    case JsNull => JsSuccess(Some(None))
                    case js     => reads.reads(js).repath(self).map(v => Some(Some(v)))
                  }
                ))
      }
    }
    // ※インデントが奇抜ですが、scalafmtの結果です。
  }

  def nameValidate    = pattern("""([^\x01-\x7E]|[\da-zA-Z]|[^\S\r\n])+""".r,"カテゴリは英数字・日本語などしか含まれていません")
  def slugValidate    = pattern("""([\da-zA-Z]|[^\S\r\n])+""".r,"slugは英数字しか含まれていません")
  def colorValidate   = pattern("""(1|2|3)""".r,"colorは数字(1,2,3)しか含まれていません")

  def bodyValidation  = pattern("""([^\x01-\x7E]|[\da-zA-Z]|[^\S])+""".r ,"本文は英数字・日本語・改行などしか含まれていません")
 	def titleValidation = pattern("""([^\x01-\x7E]|[\da-zA-Z]|[^\S\r\n])+""".r,"タイトルは英数字・日本語などしか含まれていません")
  
}