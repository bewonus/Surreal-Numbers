package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Brandon Ewonus on 16-03-24.
 */
public class Form implements Comparable<Object> {

  /** The left set. */
  private List<Double> L;

  /** The right set. */
  private List<Double> R;

  /** Default constructor. */
  Form() {
    L = new ArrayList<>();
    R = new ArrayList<>();
  }

  /** Construct a form from a left and right set. */
  Form(List<Double> L, List<Double> R) {
    this.L = L;
    this.R = R;
  }

  /** Get the left set. */
  List<Double> getL() {
    return L;
  }

  /** Get the right set. */
  List<Double> getR() {
    return R;
  }

  /** Add integers to the left set. */
  void addToL(Integer...toAdd) {
    List<Integer> toL = Arrays.asList(toAdd);
    addToL(toL.stream()
      .mapToDouble(i -> (double) i)
      .boxed()
      .collect(Collectors.toList()));
  }

  /** Add integers to the right set. */
  void addToR(Integer...toAdd) {
    List<Integer> toR = Arrays.asList(toAdd);
    addToR(toR.stream()
      .mapToDouble(i -> (double) i)
      .boxed()
      .collect(Collectors.toList()));
  }

  /** Add doubles to the left set. */
  void addToL(Double...toAdd) {
    List<Double> toL = Arrays.asList(toAdd);
    addToL(toL);
  }

  /** Add doubles to the right set. */
  void addToR(Double...toAdd) {
    List<Double> toR = Arrays.asList(toAdd);
    addToR(toR);
  }

  /** Add a list of doubles to the left set. */
  void addToL(List<Double> toAdd) {
    L.addAll(toAdd);
  }

  /** Add a list of doubles to the right set. */
  void addToR(List<Double> toAdd) {
    R.addAll(toAdd);
  }

  /**
   * Convert an integer to a form.
   *    If n < 0, then n = { | n+1 }
   *    If n > 0, then n = { n-1 | }
   *    If n == 0, then n = { | }
   */
  private static Form intToForm(Integer n) {
    List<Double> Lnew = new ArrayList<>();
    List<Double> Rnew = new ArrayList<>();
    if (n < 0) {
      Rnew.add((double) n + 1);
    }
    else if (n > 0) {
      Lnew.add((double) n - 1);
    }
    return new Form(Lnew, Rnew);
  }

  /** Convert a form to a double. */
  static Double formToDouble(Form X) {
    List<Double> XL = X.getL();
    List<Double> XR = X.getR();
    if (XL.isEmpty()) {
      if (XR.isEmpty()) {
        // A form with an empty left and right set is 0
        return (double) 0;
      }
      else {
        // A form with an empty left set is equal to min(XR) - 1
        return XR.stream().mapToDouble(d -> d).min().orElse(0) - 1;
      }
    }
    else {
      if (XR.isEmpty()) {
        // A form with an empty right set is equal to min(XL) - 1
        return XL.stream().mapToDouble(d -> d).max().orElse(0) + 1;
      }
      else {
        // A form with non-empty sets is equal to the average of min(XL) and min(XR)
        Double left = XL.stream().mapToDouble(d -> d).max().orElse(0);
        Double right = XR.stream().mapToDouble(d -> d).min().orElse(0);
        return (left + right) / 2;
      }
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Form) {
      return this.lessOrEqualTo(obj)
        && ((Form) obj).lessOrEqualTo(this);
    }
    else if (obj instanceof Integer) {
      return this.lessOrEqualTo(intToForm((Integer) obj))
        && intToForm((Integer) obj).lessOrEqualTo(this);
    }
    return false;
  }

  @Override
  public int compareTo(Object form) {
    if (this.equals(form)) {
      return 0;
    }
    else if (lessOrEqualTo(form)) {
      return -1;
    }
    else {
      return 1;
    }
  }

  /** Compares two objects which are assumed to be either forms or integers. */
  static int compare(Object a, Object b) {
    if (a instanceof Form) {
      return ((Form) a).compareTo(b);
    }
    else {
      return intToForm((Integer) a).compareTo(b);
    }
  }

  /** Return true if this form is less than or equal to the argument form. */
  private boolean lessOrEqualTo(Object form) {
    return lessOrEqualTo(this, form);
  }

  /**
   * Return true if X is less than or equal to Y. X and Y are assumed to be
   * either forms or integers.
   */
  private static boolean lessOrEqualTo(Object X, Object Y) {
    if (X instanceof Form) {
      if (Y instanceof Form) {
        return lessOrEqualTo((Form) X, (Form) Y);
      }
      else {
        return lessOrEqualTo((Form) X, intToForm((Integer) Y));
      }
    }
    else {
      if (Y instanceof Form) {
        return lessOrEqualTo(intToForm((Integer) X), (Form) Y);
      }
      else {
        return ((Integer) X) <= ((Integer) Y);
      }
    }
  }

  /**
   * Recursively compute whether form X is less than or equal to form Y.
   * Given numeric forms x = { XL | XR } and y = { YL | YR }, x ≤ y if and only if:
   *  - there is no xL ∈ XL such that y ≤ xL
   *      (every element in the left part of x is smaller than y), and
   *  - there is no yR ∈ YR such that yR ≤ x
   *      (every element in the right part of y is bigger than x).
   */
  private static boolean lessOrEqualTo(Form X, Form Y) {
    for (Double xL : X.getL()) {
      if (lessOrEqualTo(Y, intToForm((int) Math.round(xL)))) {
        return false;
      }
    }
    for (Double yR : Y.getR()) {
      if (lessOrEqualTo(intToForm((int) Math.round(yR)), X)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    String str = "{";
    int Lsize = L.size();
    if (Lsize > 0) {
      str += L.get(0);
    }
    for (int i = 1; i < Lsize; i++) {
      str += "," + L.get(i);
    }
    str += "|";
    int Rsize = R.size();
    if (Rsize > 0) {
      str += R.get(0);
    }
    for (int i = 1; i < Rsize; i++) {
      str += "," + R.get(i);
    }
    str += "}";
    return str;
  }
}
