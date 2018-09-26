/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 package com.product.aladino;

import android.content.Context;
import android.text.TextUtils;
import com.google.firebase.firestore.Query;

public class Filters {

    private String categoria = null;
    private String nombre = null;

    public Filters() {}

    public static Filters getDefault() {
        Filters filters = new Filters();
        return filters;
    }

    public boolean hasCategory() {
        return !(TextUtils.isEmpty(categoria));
    }

    public String getCategory() {
        return categoria;
    }

    public void setCategory(String category) {
        this.categoria = category;
    }

    public boolean hasName() {
        return !(TextUtils.isEmpty(nombre));
    }

    public String getName() {
        return nombre;
    }

    public void setName(String name) {
        this.nombre = name;
    }
}
