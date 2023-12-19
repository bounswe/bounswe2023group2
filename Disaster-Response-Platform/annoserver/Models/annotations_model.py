from pydantic import BaseModel, Field, HttpUrl
from typing import List, Optional

class TargetSelector(BaseModel):
    source: Optional[str]
    selector: Optional[str]

class TextQuoteSelector(BaseModel):
    exact: str
    prefix: Optional[str]
    suffix: Optional[str]

class TextPositionSelector(BaseModel):
    start: int
    end: Optional[int]

class DataPositionSelector(BaseModel):
    start: int
    end: Optional[int]

class SvgSelector(BaseModel):
    value: str

class RangeSelector(BaseModel):
    startSelector: TargetSelector
    endSelector: TargetSelector
    type: str = "Range"

class AnnotationBody(BaseModel):
    type: str
    purpose: Optional[str]
    value: Optional[str]

class ImageBody(BaseModel):
    type: str = "Image"
    format: str
    url: str
class Annotation(BaseModel):
    id: Optional[str]
    type: str = "Annotation"
    motivation: List[str]
    body: List[AnnotationBody]
    target: List[TargetSelector]

class W3CAnnotation(BaseModel):
    class Config:
        fields = {'@context': {'alias': 'context'},
                  '@type': {'alias': 'type'}}
    context: Optional[str]
    type: str = "Annotation"
    id: Optional[str]
    type: Optional[str]
    motivation: Optional[List[str]]
    creator: Optional[str]
    created: Optional[str]
    modified: Optional[str]
    generator: Optional[HttpUrl]
    body: Optional[List[AnnotationBody]]
    target: Optional[List[TargetSelector]]
    stylesheet: Optional[HttpUrl]
    renderedVia: Optional[HttpUrl]
    rights: Optional[str]
    canonical: Optional[HttpUrl]
    conformsTo: Optional[HttpUrl]
    partOf: Optional[HttpUrl]
    isBasedOn: Optional[HttpUrl]
    items: Optional[List[Annotation]]

class HypothesisModel(W3CAnnotation):
    uri:Optional[HttpUrl]

sample_for_hypothesis:HypothesisModel = {
    "uri": "http://3.218.226.215:3000/resources/annotations/65776cc6d80e93410000000b",
    "tags": [
        "api", "DAPP","rescue","resource"
    ],
    "body": {
        "type": "TextualBody",
        "value": "Kaynağın verdiği bilgi ve önerisi: Sağladığı yemeklerin [PSAKD binasından](https://maps.app.goo.gl/hq6PxMbf4N8SWNTV7) yüklenebileceğini, yakın yerlerde olursa taşımayı kendisinin yapabileceğini söylüyor. [Fotoğrafını görebilirsiniz](https://zaytung.com/voicepics//Ahmad.jpg) ",
        "format": "text/plain"
    },
    "document": {
        "title": "DAPP resource 65776cc6d80e934140000000b",
        "link": [{"href":"http://3.218.226.215:3000/resources/annotations/65776cc6d80e93414cd8300b"}]
    },
    "target": [
        {
            "source": "http://3.218.226.215:3000/resources/annotations/65776cc6d80e93414cd8300b",
            "selector": [{"type":"XPathSelector",
                "value":"http://3.218.226.215:3000/resources/annotations/65776cc6d80e93414cd8300b"}]
        }
    ],
    "user": "acct:mehmet.kuzulugil@boun.edu.tr",
    "permissions": {
        "read": [
            "group:__world__"
        ],
        "update": [
            "acct:mehmet.kuzulugil@boun.edu.tr"
        ],
        "delete": [
            "acct:mehmet.kuzulugil@boun.edu.tr"
        ],
        "admin": [
            "acct:mehmet.kuzulugil@boun.edu.tr"
        ]
    }
}


sample_annotation_data:W3CAnnotation = {
    "context": "http://www.w3.org/ns/anno.jsonld",
    "type": "Annotation",
    "id": "http://dapp.org/annotated",
    "motivation": ["commenting", "highlighting"],
    "body": [
        {"type": "TextualBody", "value": "Elbiseler soğuk hava koşulları düşünülerek edinilmelidir"},
        {"type": "TextualBody", "value": "Çocuk giysilerine öncelik verilebilir"},
        {"type": "ImageBody", "format": "image/jpeg", "url": "http://dapp.org/9102211123.jpg"},
        {"type": "URL", "url": "https://www.defacto.com.tr/takim-2812969"},
    ],
    "target": [
        {"source": "http://dapp.org/annotated/needs/", "data_selector": {"start": 0x653ff479d6b8de0728e51b15, "end": 0x653ff479d6b8de0728e51b15}},
    ],
}