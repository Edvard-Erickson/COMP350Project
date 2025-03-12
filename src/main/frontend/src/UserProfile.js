var UserProfile = (function() {
  var majors = [];
  var minors = [];

  var getMajors = function() {
    return majors;
  };

  var addMajor = function(major) {
    majors.push(major);
  };

  var getMinors = function() {
    return minors;
  };

  var addMinor = function(minor) {
    minors.push(minor);
  };

  return {
    getMajors: getMajors,
    addMajor: addMajor,
    getMinors: getMinors,
    addMinor: addMinor
  }

})();

export default UserProfile;