
        // Password strength indicator
        document.getElementById('credentials.password').addEventListener('input', function() {
            const password = this.value;
            const strengthBar = document.querySelector('.password-strength-bar');
            let strength = 0;
            
            if (password.length >= 8) strength += 25;
            if (password.match(/[a-z]/)) strength += 25;
            if (password.match(/[A-Z]/)) strength += 25;
            if (password.match(/[0-9]/)) strength += 25;
            
            strengthBar.style.width = strength + '%';
        });
        
        // Form validation
        // Note: The 'confermaPassword' input was commented out in your HTML.
        // If you re-introduce it, uncomment this JavaScript block as well.
        /*
        document.querySelector('form').addEventListener('submit', function(e) {
            const password = document.getElementById('credentials.password').value;
            const confermaPassword = document.getElementById('confermaPassword').value;
            
            if (password !== confermaPassword) {
                e.preventDefault();
                alert('Le password non coincidono!');
            }
        });
      */