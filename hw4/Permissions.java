https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package hw4;

/**
 * Class representing requested permissions to a relation/file.
 * Private constructor with two static objects READ_ONLY and READ_WRITE that
 * represent the two levels of permission.
 */
public class Permissions {
  int permLevel;

  private Permissions(int permLevel) {
    this.permLevel = permLevel;
  }

  public String toString() {
    if (permLevel == 0)
      return "READ_ONLY";
    if (permLevel == 1)
      return "READ_WRITE";
    return "UNKNOWN";
  }

  public static final Permissions READ_ONLY = new Permissions(0);
  public static final Permissions READ_WRITE = new Permissions(1);

}
