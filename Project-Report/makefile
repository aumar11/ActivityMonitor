all: template.pdf

template.pdf: *.tex *.bib
	xelatex template
	bibtex -terse template
	xelatex template
	xelatex template
  
clobber:
	rm -f *.out *.dvi *.aux *.log *.lof *.lot *.toc *.bbl *.blg *.brf *.mlf* *.mlt* *.bmt *.maf
	rm -f */*.aux
  
clean: clobber
	rm -f Thesis.pdf