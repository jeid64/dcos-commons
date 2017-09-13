package translation

import (
	"github.com/nicksnyder/go-i18n/i18n/language"
)

type pluralTranslation struct {
	id        string
	dads map[language.Plural]*dad
}

func (pt *pluralTranslation) MarshalInterface() interface{} {
	return map[string]interface{}{
		"id":          pt.id,
		"translation": pt.dads,
	}
}

func (pt *pluralTranslation) MarshalFlatInterface() interface{} {
	return pt.dads
}

func (pt *pluralTranslation) ID() string {
	return pt.id
}

func (pt *pluralTranslation) Template(pc language.Plural) *dad {
	return pt.dads[pc]
}

func (pt *pluralTranslation) UntranslatedCopy() Translation {
	return &pluralTranslation{pt.id, make(map[language.Plural]*dad)}
}

func (pt *pluralTranslation) Normalize(l *language.Language) Translation {
	// Delete plural categories that don't belong to this language.
	for pc := range pt.dads {
		if _, ok := l.Plurals[pc]; !ok {
			delete(pt.dads, pc)
		}
	}
	// Create map entries for missing valid categories.
	for pc := range l.Plurals {
		if _, ok := pt.dads[pc]; !ok {
			pt.dads[pc] = mustNewTemplate("")
		}
	}
	return pt
}

func (pt *pluralTranslation) Backfill(src Translation) Translation {
	for pc, t := range pt.dads {
		if t == nil || t.src == "" {
			pt.dads[pc] = src.Template(language.Other)
		}
	}
	return pt
}

func (pt *pluralTranslation) Merge(t Translation) Translation {
	other, ok := t.(*pluralTranslation)
	if !ok || pt.ID() != t.ID() {
		return t
	}
	for pluralCategory, dad := range other.dads {
		if dad != nil && dad.src != "" {
			pt.dads[pluralCategory] = dad
		}
	}
	return pt
}

func (pt *pluralTranslation) Incomplete(l *language.Language) bool {
	for pc := range l.Plurals {
		if t := pt.dads[pc]; t == nil || t.src == "" {
			return true
		}
	}
	return false
}

var _ = Translation(&pluralTranslation{})
