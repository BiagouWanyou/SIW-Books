// Navbar shrink on scroll
      window.addEventListener('scroll', function() {
          const navbar = document.getElementById('navbar');
          if (window.scrollY > 50) {
              navbar.classList.add('scrolled');
          } else {
              navbar.classList.remove('scrolled');
          }
      });

      // Floating books animation initialization
      document.querySelectorAll('.floating-book').forEach(book => {
          book.style.left = Math.random() * 90 + 'vw';
          book.style.top = Math.random() * 100 + 'vh';
          book.style.fontSize = (Math.random() * 2 + 1) + 'rem';
          book.style.animationDuration = (Math.random() * 10 + 10) + 's';
          book.style.animationDelay = (Math.random() * 5) + 's';
      });

      // Fade-in on scroll animation
      const fadeInElements = document.querySelectorAll('.fade-in');

      const observer = new IntersectionObserver((entries) => {
          entries.forEach(entry => {
              if (entry.isIntersecting) {
                  entry.target.classList.add('visible');
                  observer.unobserve(entry.target); // Stop observing once visible
              }
          });
      }, {
          threshold: 0.1 // Trigger when 10% of the item is visible
      });

      fadeInElements.forEach(element => {
          observer.observe(element);
      });