/*
 * #%L
 * Common package for I/O and related utilities
 * %%
 * Copyright (C) 2017 Open Microscopy Environment:
 *   - Board of Regents of the University of Wisconsin-Madison
 *   - Glencoe Software, Inc.
 *   - University of Dundee
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package ome.files.performance;

import java.io.Writer;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

class Result {
  private Writer writer;
  private PrintWriter output;
  ResultMap<String,Object> customParams = new ResultMap<String,Object>();

  Result(Path filename) throws java.io.IOException {
    writer = new FileWriter(filename.toString());
    output = new PrintWriter(writer);
    output.println("test.lang\ttest.name\ttest.file\t"+customParams.keyToString()+"treal\tproc.cpu\tproc.user\tproc.system");
  }
  
  void addCustomParam(String heading, Object value) {
    customParams.put(heading, value);
  }

  void add(String testname,
           Path testfile,
           Timepoint start,
           Timepoint end) {
    output.println("Java\t" + testname + "\t" +
                   testfile.getFileName().toString() + "\t" +
                   customParams.valueToString() +
                   (end.real-start.real)/1000000 + "\t" +
                   (end.cpu-start.cpu)/1000000 + "\t" +
                   (end.user-start.user)/1000000 + "\t" +
                   (end.system-start.system)/1000000);

  }
  
  void add(String testname, Path testfile, double size) {
output.println("Java\t" + testname + "\t" +
              testfile.getFileName().toString() + "\t" +
              customParams.valueToString() +
              0 + "\t" +
              0 + "\t" +
              0 + "\t" +
              0 + "\t" + size);
}

  void close() {
    output.close();
  }
  
  private class ResultMap<K,V> extends HashMap<K,V> {

    public String keyToString() {
        String keyString = "";
        for (Map.Entry<K, V> entry : this.entrySet()) {
            keyString += entry.getKey();
            keyString += "\t";
        }
        return keyString;
    }
    
    public String valueToString() {
      String valueString = "";
      for (Map.Entry<K, V> entry : this.entrySet()) {
          valueString += entry.getKey();
          valueString += "\t";
      }
      return valueString;
    }
  }
}
