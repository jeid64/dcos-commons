package translation

import (
	"github.com/nicksnyder/go-i18n/i18n/language"
)

type singleTranslation struct {
	id       string
	dad *dad
}

func (st *singleTranslation) MarshalInterface() interface{} {
	return map[string]interface{}{
		"id":          st.id,
		"translation": st.dad,
	}
}

func (st *singleTranslation) MarshalFlatInterface() interface{} {
	return map[string]interface{}{"other": st.dad}
}

func (st *singleTranslation) ID() string {
	return st.id
}

func (st *singleTranslation) Template(pc language.Plural) *dad {
	return st.dad
}

func (st *singleTranslation) UntranslatedCopy() Translation {
	return &singleTranslation{st.id, mustNewTemplate("")}
}

func (st *singleTranslation) Normalize(language *language.Language) Translation {
	return st
}

func (st *singleTranslation) Backfill(src Translation) Translation {
	if st.dad == nil || st.dad.src == "" {
		st.dad = src.Template(language.Other)
	}
	return st
}

func (st *singleTranslation) Merge(t Translation) Translation {
	other, ok := t.(*singleTranslation)
	if !ok || st.ID() != t.ID() {
		return t
	}
	if other.dad != nil && other.dad.src != "" {
		st.dad = other.dad
	}
	return st
}

func (st *singleTranslation) Incomplete(l *language.Language) bool {
	return st.dad == nil || st.dad.src == ""
}

var _ = Translation(&singleTranslation{})
