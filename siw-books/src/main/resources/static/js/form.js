document.getElementById('file').addEventListener('change', function(e) {
            const label = document.getElementById('file-label');
            const fileName = e.target.files[0]?.name;
            if (fileName) {
                label.textContent = `File selezionato: ${fileName}`;
            } else {
                label.textContent = 'Seleziona una foto dell\'autore';
            }
        });
		
const sinossiTextarea = document.getElementById('sinossi');
		     if (sinossiTextarea) {
		           const charCounter = document.createElement('div');
		           charCounter.className = 'text-muted small mt-1';
		           charCounter.style.textAlign = 'right';
		           sinossiTextarea.parentNode.appendChild(charCounter);
		           
		           function updateCharCount() {
		               const current = sinossiTextarea.value.length;
		               const max = 1000;
		               charCounter.textContent = `${current}/${max} caratteri`;
		               if (current > max * 0.9) {
		                   charCounter.style.color = '#dc3545';
		               } else {
		                   charCounter.style.color = '#6c757d';
		               }
		           }
		           
		           sinossiTextarea.addEventListener('input', updateCharCount);
		           updateCharCount();
		       }	
			   document.addEventListener('DOMContentLoaded', function() {
			       // Select all dropdowns that have a button with the class 'custom-dropdown'
			       const dropdowns = document.querySelectorAll('.dropdown');

			       dropdowns.forEach(dropdown => {
			           const dropdownButton = dropdown.querySelector('.custom-dropdown');
			           const dropdownMenu = dropdown.querySelector('.dropdown-menu');
			           const checkboxes = dropdownMenu.querySelectorAll('input[type="checkbox"]');

			           // Prevent the dropdown from closing on a menu click
			           dropdownMenu.addEventListener('click', function(event) {
			               event.stopPropagation();
			           });

			           // Function to update the button text for this specific dropdown
			           function updateButtonText() {
			               const checkedItems = Array.from(checkboxes)
			                   .filter(cb => cb.checked)
			                   .map(cb => {
			                       // Check if the next sibling is the text span
			                       const nextSibling = cb.nextElementSibling;
			                       if (nextSibling && nextSibling.tagName === 'SPAN') {
			                           return nextSibling.textContent.trim();
			                       }
			                       // Or if it's the checkmark, get the one after that
			                       return cb.nextElementSibling.nextElementSibling.textContent.trim();
			                   });
			               
			              
			           }
			           
			           // Listen for changes on all checkboxes within this dropdown
			           checkboxes.forEach(checkbox => {
			               checkbox.addEventListener('change', updateButtonText);
			           });

			           // Initial update
			           updateButtonText();
			       });
			   });