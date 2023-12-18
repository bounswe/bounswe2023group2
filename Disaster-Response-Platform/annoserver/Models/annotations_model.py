from pydantic import BaseModel, Field, HttpUrl
from typing import List, Optional
# Annotation olayı bayağı basitleşecek
# Kullandığımız servis halledecek. Sadece oraya gideacek olanlar için API call vs.



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


sample_annotation_data = {
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