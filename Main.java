package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Brandon Ewonus on 16-03-24.
 */
public class Main {

    public static void main(String[] args) {
      // Create some forms
      Form zero = new Form();
      Form X = new Form();
      X.addToL(0);
      X.addToR(1);
      Form Y = new Form();
      Y.addToL(-1, 0);
      Y.addToR(1);

      // Add some forms to a list
      List<Object> forms = new ArrayList<Object>();
      forms.add(zero);
      forms.add(X);
      forms.add(Y);
      forms.add(-2);
      forms.add(-1);
      forms.add(0);
      forms.add(1);
      forms.add(2);

      // Check that the comparison function for forms is functioning properly
      Collections.sort(forms, Form::compare);
      printOrdered(forms);
    }

  /**
   * Print the sorted forms in a nice format.
   * @param forms a list of sorted forms
   */
  private static void printOrdered(List<Object> forms) {
    if (forms.size() >= 1) {
      String str = "";
      str += forms.get(0);
      for (int i = 1; i < forms.size(); i++) {
        if (forms.get(i - 1).equals(forms.get(i))) {
          str += " = " + forms.get(i);
        }
        else {
          str += " < " + forms.get(i);
        }
      }
      System.out.println(str);
    }
  }

  /** Print X and Y with the correct comparison sign between them. */
  private static void printCompareTo(Form X, Form Y) {
    switch (X.compareTo(Y)) {
      case -1:
        System.out.println(X + " < " + Y);
        break;
      case 0:
        System.out.println(X + " = " + Y);
        break;
      case 1: default:
        System.out.println(X + " > " + Y);
        break;
    }
  }
}
