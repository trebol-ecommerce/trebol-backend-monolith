/*
 * Copyright (c) 2023 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.jpa.services.patch.impl;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.trebol.api.models.ImagePojo;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.services.patch.ImagesPatchService;

@Service
@NoArgsConstructor
public class ImagesPatchServiceImpl
  implements ImagesPatchService {

  @Override
  public Image patchExistingEntity(ImagePojo changes, Image existing) {
    Image target = new Image(existing);

    String code = changes.getCode();
    if (code != null && !code.isBlank() && !target.getCode().equals(code)) {
      target.setCode(code);
    }

    String filename = changes.getFilename();
    if (filename != null && !filename.isBlank() && !target.getFilename().equals(filename)) {
      target.setFilename(filename);
    }

    String url = changes.getUrl();
    if (url != null && !url.isBlank() && !target.getUrl().equals(url)) {
      target.setUrl(url);
    }

    return target;
  }
}
