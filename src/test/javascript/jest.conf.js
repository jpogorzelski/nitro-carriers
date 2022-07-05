module.exports = {
    preset: 'jest-preset-angular',
    setupFilesAfterEnv: ['<rootDir>/src/test/javascript/jest.ts'],
    coverageDirectory: '<rootDir>/build/test-results/',
    globals: {
        'ts-jest': {
            stringifyContentPathRegex: '\\.html?$',
            tsConfig: 'tsconfig.json',
        },
    },
    coveragePathIgnorePatterns: [
        '<rootDir>/src/test/javascript',
    ],
    moduleNameMapper: {
        'app/(.*)': '<rootDir>/src/main/webapp/app/$1',
    },
    reporters: [
        'default',
        [
            'jest-junit',
            {
                output: './build/test-results/TESTS-results-jest.xml',
            },
        ],
    ],
    testResultsProcessor: 'jest-sonar-reporter',
    transformIgnorePatterns: [
        'node_modules/(?!@angular/common/locales)',
    ],
    testMatch: [
        '<rootDir>/src/test/javascript/spec/**/+(*.)+(spec.ts)',
    ],
    rootDir: '../../../',
    testURL: 'http://localhost/',
    testRegex: [],
}
;
